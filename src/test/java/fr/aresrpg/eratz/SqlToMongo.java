package fr.aresrpg.eratz;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.database.Collection;
import fr.aresrpg.commons.domain.database.Filter;
import fr.aresrpg.commons.domain.serialization.factory.SerializationFactory;
import fr.aresrpg.commons.infra.database.mongodb.MongoDBDatabase;
import fr.aresrpg.commons.infra.serialization.unsafe.UnsafeSerializationFactory;
import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.util.Compressor;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.infra.map.BotMapImpl;
import fr.aresrpg.eratz.infra.map.DestinationImpl;
import fr.aresrpg.eratz.infra.map.adapter.BotMapAdapter;
import fr.aresrpg.eratz.infra.map.adapter.TriggerAdapter;
import fr.aresrpg.eratz.infra.map.dao.BotMapDao;
import fr.aresrpg.eratz.infra.map.trigger.InterractableTrigger;
import fr.aresrpg.eratz.infra.map.trigger.TeleporterTrigger;
import fr.aresrpg.eratz.infra.map.trigger.TeleporterTrigger.TeleportType;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class SqlToMongo {

	static final SerializationFactory factory = new UnsafeSerializationFactory();
	static Collection<BotMapDao> mongoColl;
	static java.sql.Connection dbConnect = null;
	static java.sql.Statement dbStatement = null;
	static Map<Integer, Set<TeleporterTrigger>> triggg = new HashMap<>();
	static int count;
	static Map<Integer, BotMap> allMaps = new HashMap<>();

	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		loadJar(ClassLoader.getSystemClassLoader());
		initMongo();
		factory.addAdapter(TriggerAdapter.IDENTITY);
		connect();
		maps();
		Threads.uSleep(3, TimeUnit.SECONDS);
		System.out.println("Starting publishing...");
		publish();
		System.out.println("\n Finished !");
	}

	static void publish() {
		allMaps.values().forEach(SqlToMongo::pushBotMap);
	}

	static void initMongo() throws IOException {
		MongoDBDatabase mongoDBDatabase = new MongoDBDatabase("botfather");
		mongoDBDatabase.connect("localhost", 27017, "sceat", "netgear2000");
		mongoColl = mongoDBDatabase.get("maps", BotMapDao.class);
	}

	static int index = 0;

	static void pushBotMap(BotMap m) {
		mongoColl.putOrUpdate(Filter.eq("mapid", m.getMapId()), BotMapAdapter.IDENTITY.adaptTo((BotMapImpl) m));
		System.out.println("[" + (++index) + "] Publishing " + m);
	}

	static void connect() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		dbConnect = DriverManager.getConnection("jdbc:mysql://localhost:3306/dofus_map", "root", "");
		dbStatement = dbConnect.createStatement();
		System.out.println("Connected !");
	}

	static void retrieve() {
		BotMapDao[] find = mongoColl.find(null, (int) mongoColl.count());
		System.out.println("Size = " + find.length);
	}

	static ResultSet exec(String sql) throws SQLException {
		ResultSet rs = dbStatement.executeQuery(sql);
		return rs;
	}

	static void maps() throws SQLException {
		ResultSet exec2 = exec("SELECT * FROM `trigger`");
		int v = 0;
		while (exec2.next()) {
			SqlTrigger sqlTrigger = new SqlTrigger(exec2.getInt(1), exec2.getInt(2), exec2.getInt(3), exec2.getInt(4));
			toTp(sqlTrigger);
			System.out.println("Teleporter [" + ++v + "]");
		}

		ResultSet exec = exec("SELECT * FROM static_maps");
		while (exec.next()) {
			int id = exec.getInt(1);
			String date = exec.getString(2);
			String decrypted = exec.getString(5);
			String pos = exec.getString(6);
			int w = exec.getInt(7);
			int h = exec.getInt(8);
			int ar = exec.getInt(9);
			SqlMap sqlMap = new SqlMap(id, date, decrypted, pos, w, h, ar);
			BotMap bm = tomMap(sqlMap);
			if (bm == null) {
				System.out.println("data null !");
				continue;
			}
			System.out.println("[" + (++count) + "] " + bm);
			BotMap botMap = allMaps.get(bm.getMapId());
			if (botMap == null || botMap.getTimeMs() < bm.getTimeMs()) allMaps.put(bm.getMapId(), bm);
		}
	}

	static BotMap tomMap(SqlMap m) {
		BotMapImpl botMapImpl = new BotMapImpl(m.id, m.date.getTimeInMillis(), null, m.x, m.y, m.width, m.height);
		if (m.id == -1) return null;
		Cell[] cells = Compressor.uncompressMap(m.decryptedData);
		Set<Trigger> tr = new HashSet<>();
		for (Cell c : cells) {
			if (Interractable.isZaap(c.getLayerObject2Num())) tr.add(new TeleporterTrigger(c.getId(), TeleportType.ZAAP, new DestinationImpl(-1, -1)));
			else if (Interractable.isZaapi(c.getLayerObject2Num())) tr.add(new TeleporterTrigger(c.getId(), TeleportType.ZAAPI, new DestinationImpl(-1, -1)));
			else if (Interractable.isInterractable(c.getLayerObject2Num())) tr.add(new InterractableTrigger(c.getId(), c.getLayerObject2Num()));
		}
		Set<TeleporterTrigger> set = triggg.get(m.id);
		if (set != null) set.forEach(tr::add);
		botMapImpl.setTriggers(BotMapAdapter.IDENTITY.readTriggers(tr.stream().toArray(Trigger[]::new)));
		return botMapImpl;
	}

	static Set<TeleporterTrigger> getT(int mapid) {
		Set<TeleporterTrigger> set = triggg.get(mapid);
		if (set == null) triggg.put(mapid, set = new HashSet<>());
		return set;
	}

	static void toTp(SqlTrigger tr) {
		getT(tr.mapid).add(new TeleporterTrigger(tr.cellid, TeleportType.MAP_TP, new DestinationImpl(tr.mapcible, tr.cellcible)));
	}

	public static class SqlTrigger {
		int mapid, cellid, mapcible, cellcible;

		public SqlTrigger(int mapid, int cellid, int mapcible, int cellcible) {
			this.mapid = mapid;
			this.cellid = cellid;
			this.mapcible = mapcible;
			this.cellcible = cellcible;
		}

		@Override
		public String toString() {
			return "SqlTrigger [mapid=" + mapid + ", cellid=" + cellid + ", mapcible=" + mapcible + ", cellcible=" + cellcible + "]";
		}

	}

	public static class SqlMap {
		int id;
		Calendar date = Calendar.getInstance();
		String decryptedData;
		int x, y, width, height, area;

		public SqlMap(int id, String instant, String decryptedData, String coords, int width, int height, int area) {
			this.id = id;
			if (coords == null || decryptedData == null || instant == null) this.id = -1;
			else {
				this.decryptedData = decryptedData;
				String[] c = coords.split(",");
				this.x = Integer.parseInt(c[0]);
				this.y = Integer.parseInt(c[1]);
				this.width = width;
				this.height = height;
				this.area = area;
			}
		}

		void parseDate(String date) {
			this.date.set(Integer.parseInt(date.substring(0, 2)), Integer.parseInt(date.substring(2, 4)), Integer.parseInt(date.substring(4, 6)), Integer.parseInt(date.substring(6, 8)),
					Integer.parseInt(date.substring(8)));
		}

		@Override
		public String toString() {
			return "SqlMap [id=" + id + ", date=" + date.getTime() + ", decryptedData=" + decryptedData.substring(0, 50) + ", x=" + x + ", y=" + y + ", width=" + width + ", height=" + height
					+ ", area=" + area
					+ "]";
		}

	}

	private static void addUrl(ClassLoader loader, URL[] urls) {
		Method method;
		try {
			method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
			method.setAccessible(true);
			method.invoke(loader, (Object[]) urls);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadJar(ClassLoader loader) {
		File file = new File(".");
		if (file.listFiles().length == 0) return;
		for (File f : file.listFiles()) {
			if (f.getName().contains(".jar")) {
				URL url;
				try {
					url = new URL("jar", "", f.toURI().toURL() + "!/");
					URL[] urls = new URL[] { url };
					addUrl(loader, urls);
					System.out.println("loaded " + f.getName());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
