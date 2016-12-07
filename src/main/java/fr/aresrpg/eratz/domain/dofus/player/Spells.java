/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.dofus.player;

import fr.aresrpg.eratz.domain.player.Perso;

/**
 * 
 * @since
 */
public enum Spells {

	LA_BLOQUEUSE(Classe.SADIDA, 193, SpellCharac.INVOCATION),
	RONCE(Classe.SADIDA, 183, SpellCharac.TERRE),
	POISON_PARALYSANT(Classe.SADIDA, 200, SpellCharac.FEU),
	SACRIFICE_POUPESQUE(Classe.SADIDA, 198, SpellCharac.BUFF),
	LARME(Classe.SADIDA, 195, SpellCharac.EAU),
	LA_FOLLE(Classe.SADIDA, 182, SpellCharac.INVOCATION),
	RONCE_APAISANTE(Classe.SADIDA, 192, SpellCharac.BUFF),
	PUISSANCE_SYLVESTRE(Classe.SADIDA, 197, SpellCharac.BUFF),
	LA_SACRIFIEE(Classe.SADIDA, 189, SpellCharac.INVOCATION),
	TREMBLEMENT(Classe.SADIDA, 181, SpellCharac.FEU),
	CONNAISSANCE_DES_POUPEES(Classe.SADIDA, 199, SpellCharac.BUFF),
	RONCE_MULTIPLE(Classe.SADIDA, 191, SpellCharac.TERRE),
	ARBRE(Classe.SADIDA, 186, SpellCharac.INVOCATION),
	VENT_EMPOISONE(Classe.SADIDA, 196, SpellCharac.NEUTRE),
	LA_GONFLABLE(Classe.SADIDA, 190, SpellCharac.INVOCATION, SpellCharac.HEAL),
	RONCE_AGRESSIVE(Classe.SADIDA, 194, SpellCharac.TERRE),
	HERBE_FOLLE(Classe.SADIDA, 185, SpellCharac.FEU),
	FEU_DE_BROUSSE(Classe.SADIDA, 184, SpellCharac.FEU, SpellCharac.EAU),
	RONCE_INSOLENTE(Classe.SADIDA, 188, SpellCharac.BUFF),
	LA_SURPUISSANTE(Classe.SADIDA, 187, SpellCharac.INVOCATION),
	DOPEUL_SADIDA(Classe.SADIDA, 1910, SpellCharac.INVOCATION),

	CRI_OURS(Classe.OSAMODAS, 23, SpellCharac.BUFF),
	INVOCATION_TOFU(Classe.OSAMODAS, 34, SpellCharac.INVOCATION),
	GRIFFE_SPECTRALE(Classe.OSAMODAS, 21, SpellCharac.FEU),
	BENEDICTION_ANIMALE(Classe.OSAMODAS, 26, SpellCharac.BUFF),
	DEPLACEMENT_FELIN(Classe.OSAMODAS, 22, SpellCharac.BUFF),
	INVOCATION_DE_BOUFTOU(Classe.OSAMODAS, 35, SpellCharac.INVOCATION),
	CRAPAUD(Classe.OSAMODAS, 28, SpellCharac.BUFF),
	INVOCATION_DE_PRESPIC(Classe.OSAMODAS, 37, SpellCharac.INVOCATION),
	FOUET(Classe.OSAMODAS, 30, SpellCharac.NEUTRE),
	PIQURE_MOTIVANE(Classe.OSAMODAS, 27, SpellCharac.BUFF),
	CORBEAU(Classe.OSAMODAS, 24, SpellCharac.FEU),
	GRIFFE_CINGLANTE(Classe.OSAMODAS, 33, SpellCharac.EAU),
	SOIN_ANIMAL(Classe.OSAMODAS, 25, SpellCharac.HEAL),
	INVOCATION_DE_SANGLIER(Classe.OSAMODAS, 38, SpellCharac.INVOCATION),
	FRAPPE_DU_CRAQUELEUR(Classe.OSAMODAS, 36, SpellCharac.FEU),
	RESISTANCE_NATURELLE(Classe.OSAMODAS, 32, SpellCharac.BUFF),
	CROC_DU_MULOU(Classe.OSAMODAS, 29, SpellCharac.BUFF),
	INVOCATION_DE_BWORK_MAGE(Classe.OSAMODAS, 39, SpellCharac.INVOCATION),
	INVOCATION_DE_CRAQUELEUR(Classe.OSAMODAS, 40, SpellCharac.INVOCATION),
	INVOCATION_DE_DRAGONNET_ROUGE(Classe.OSAMODAS, 31, SpellCharac.INVOCATION),
	INVOCATION_DE_DOPEUL_OSAMODAS(Classe.OSAMODAS, 1902, SpellCharac.INVOCATION),

	LANCER_DE_PIECE(Classe.ENUTROF, 51, SpellCharac.EAU),
	SAC_ANIME(Classe.ENUTROF, 41, SpellCharac.INVOCATION),
	LANCER_DE_PELLE(Classe.ENUTROF, 43, SpellCharac.TERRE),
	PELLE_FANTOMATIQUE(Classe.ENUTROF, 49, SpellCharac.FEU),
	CHANCE(Classe.ENUTROF, 42, SpellCharac.BUFF),
	BOITE_DE_PANDORE(Classe.ENUTROF, 47, SpellCharac.BUFF, SpellCharac.HEAL),
	REMBLAI(Classe.ENUTROF, 48, SpellCharac.TERRE),
	CLE_REDUCTRICE(Classe.ENUTROF, 45, SpellCharac.BUFF),
	FORCE_AGE(Classe.ENUTROF, 53, SpellCharac.AIR),
	DESINVOCATION(Classe.ENUTROF, 46, SpellCharac.AIR),
	CUPIDITE(Classe.ENUTROF, 52, SpellCharac.BUFF),
	ROULAGE_DE_PELLE(Classe.ENUTROF, 44, SpellCharac.TERRE),
	MALADRESSE(Classe.ENUTROF, 50, SpellCharac.BUFF),
	MALADRESSE_DE_MASSE(Classe.ENUTROF, 54, SpellCharac.BUFF),
	ACCELERATION(Classe.ENUTROF, 55, SpellCharac.BUFF),
	PELLE_DU_JUGEMENT(Classe.ENUTROF, 56, SpellCharac.EAU),
	PELLE_MASSACRANTE(Classe.ENUTROF, 58, SpellCharac.EAU),
	CORRUPTION(Classe.ENUTROF, 59, SpellCharac.BUFF),
	PELLE_ANIME(Classe.ENUTROF, 57, SpellCharac.INVOCATION),
	COFFRE_ANIME(Classe.ENUTROF, 60, SpellCharac.INVOCATION),
	INVOCATION_DE_DOPEUL_ENUTROF(Classe.ENUTROF, 1903, SpellCharac.INVOCATION),

	PIEGE_SOURNOIS(Classe.SRAM, 65, SpellCharac.TERRE),
	INVISIBILITE(Classe.SRAM, 72, SpellCharac.BUFF),
	SOURNOISERIE(Classe.SRAM, 61, SpellCharac.TERRE),
	POISON_INSIDIEUX(Classe.SRAM, 66, SpellCharac.AIR),
	FOURVOIEMENT(Classe.SRAM, 68, SpellCharac.AIR),
	COUP_SOURNOIS(Classe.SRAM, 63, SpellCharac.TERRE),
	DOUBLE(Classe.SRAM, 74, SpellCharac.INVOCATION),
	REPERAGE(Classe.SRAM, 64, SpellCharac.BUFF),
	PIEGE_DE_MASSE(Classe.SRAM, 79, SpellCharac.TERRE),
	INVISIBILITE_AUTRUI(Classe.SRAM, 78, SpellCharac.BUFF),
	PIEGE_EMPOISONNE(Classe.SRAM, 71, SpellCharac.TERRE),
	CONCENTRATION_DE_CHAKRA(Classe.SRAM, 62, SpellCharac.BUFF),
	PIEGE_IMMO(Classe.SRAM, 69, SpellCharac.BUFF),
	PIEGE_DE_SILENCE(Classe.SRAM, 77, SpellCharac.BUFF),
	PIEGE_REPULSIF(Classe.SRAM, 73, SpellCharac.BUFF),
	PEUR(Classe.SRAM, 67, SpellCharac.BUFF),
	ARNAQUE(Classe.SRAM, 70, SpellCharac.AIR),
	PULSION_DE_CHAKRA(Classe.SRAM, 75, SpellCharac.BUFF),
	ATTAQUE_MORTELLE(Classe.SRAM, 76, SpellCharac.TERRE),
	PIEGE_MORTEL(Classe.SRAM, 79, SpellCharac.TERRE),
	INVOCATION_DE_DOPEUL_SRAM(Classe.SRAM, 1904, SpellCharac.INVOCATION),

	RALENTISSEMENT(Classe.XELOR, 81, SpellCharac.BUFF),
	AIGUILLE(Classe.XELOR, 83, SpellCharac.INVOCATION),
	CONTRE(Classe.XELOR, 82, SpellCharac.BUFF),
	GELURE(Classe.XELOR, 84, SpellCharac.AIR),
	SABLIER_DE_XELOR(Classe.XELOR, 100, SpellCharac.FEU),
	RAYON_OBSCUR(Classe.XELOR, 92, SpellCharac.FEU),
	TELEPORTATION_XEL(Classe.XELOR, 88, SpellCharac.INVOCATION),
	FLETRISSEMENT(Classe.XELOR, 93, SpellCharac.AIR),
	FLOU(Classe.XELOR, 85, SpellCharac.BUFF),
	POUSSIERE_TEMPORELLE(Classe.XELOR, 96, SpellCharac.FEU),
	VOL_DU_TEMPS(Classe.XELOR, 98, SpellCharac.BUFF),
	AIGUILLE_CHERCHEUSE(Classe.XELOR, 86, SpellCharac.INVOCATION),
	DEVOUEMENT(Classe.XELOR, 89, SpellCharac.BUFF),
	FUITE(Classe.XELOR, 90, SpellCharac.BUFF),
	DEMOTIVATION(Classe.XELOR, 87, SpellCharac.BUFF),
	PROTECTION_AVEUGLANTE(Classe.XELOR, 94, SpellCharac.BUFF),
	MOMIFICATION(Classe.XELOR, 99, SpellCharac.BUFF),
	HORLOGE(Classe.XELOR, 95, SpellCharac.EAU),
	FRAPPE_DE_XELOR(Classe.XELOR, 91, SpellCharac.TERRE),
	CADRAN_DE_XELOR(Classe.XELOR, 97, SpellCharac.INVOCATION),
	INVOCATION_DE_DOPEUL_XELOR(Classe.XELOR, 1905, SpellCharac.INVOCATION),

	PICOLE(Classe.PANDAWA, 686, SpellCharac.BUFF),
	GEULE_DE_BOIS(Classe.PANDAWA, 692, SpellCharac.TERRE),
	POING_ENFLAMME(Classe.PANDAWA, 687, SpellCharac.FEU),
	EPOUVANTE(Classe.PANDAWA, 689, SpellCharac.BUFF),
	SOUFFLE_ALCOOLISE(Classe.PANDAWA, 690, SpellCharac.AIR),
	VULNERABILITE_AQUEUSE(Classe.PANDAWA, 691, SpellCharac.BUFF),
	VULNERABILITE_INCANDESCENTE(Classe.PANDAWA, 688, SpellCharac.BUFF),
	KARCHAM(Classe.PANDAWA, 693, SpellCharac.BUFF),
	VULNERABILITE_VENTEUSE(Classe.PANDAWA, 694, SpellCharac.BUFF),
	STABILISATION(Classe.PANDAWA, 695, SpellCharac.BUFF),
	CHAMRAK(Classe.PANDAWA, 696, SpellCharac.BUFF),
	VULNERABILITE_TERRESTE(Classe.PANDAWA, 697, SpellCharac.BUFF),
	SOUILLURE(Classe.PANDAWA, 698, SpellCharac.BUFF),
	LAIT_DE_BAMBOU(Classe.PANDAWA, 699, SpellCharac.BUFF),
	VAGUE_A_LAME(Classe.PANDAWA, 700, SpellCharac.EAU),
	COLERE_DE_ZATOISHWAN(Classe.PANDAWA, 701, SpellCharac.BUFF),
	FLASQUE_EXPLOSIVE(Classe.PANDAWA, 702, SpellCharac.FEU),
	PANDATAK(Classe.PANDAWA, 703, SpellCharac.TERRE),
	PANDANLKU(Classe.PANDAWA, 704, SpellCharac.BUFF),
	LIEN_SPIRITUEUX(Classe.PANDAWA, 705, SpellCharac.INVOCATION),
	INVOCATION_DE_DOPEUL_PANDAWA(Classe.PANDAWA, 1912, SpellCharac.INVOCATION),

	PILE_OU_FACE(Classe.ECAFLIP, 102, SpellCharac.TERRE),
	CHANCE_ECAFLIP(Classe.ECAFLIP, 103, SpellCharac.BUFF, SpellCharac.HEAL),
	BOND_DU_FELIN(Classe.ECAFLIP, 105, SpellCharac.BUFF),
	BLUFF(Classe.ECAFLIP, 109, SpellCharac.AIR, SpellCharac.EAU),
	PERCEPTION(Classe.ECAFLIP, 113, SpellCharac.BUFF),
	CONTRECOUP(Classe.ECAFLIP, 111, SpellCharac.BUFF),
	TREFLE(Classe.ECAFLIP, 104, SpellCharac.BUFF),
	TOUT_OU_RIEN(Classe.ECAFLIP, 119, SpellCharac.NEUTRE, SpellCharac.HEAL),
	ROULETTE(Classe.ECAFLIP, 101, SpellCharac.BUFF),
	TOPKAJ(Classe.ECAFLIP, 107, SpellCharac.FEU),
	LANGUE_RAPEUSE(Classe.ECAFLIP, 116, SpellCharac.FEU),
	ROUE_DE_LA_FORTUNE(Classe.ECAFLIP, 106, SpellCharac.BUFF),
	GRIFFE_INVOCATRICE(Classe.ECAFLIP, 117, SpellCharac.INVOCATION),
	ESPRIT_FELIN(Classe.ECAFLIP, 108, SpellCharac.TERRE),
	ODORAT(Classe.ECAFLIP, 115, SpellCharac.BUFF),
	REFLEXE(Classe.ECAFLIP, 118, SpellCharac.BUFF),
	GRIFFE_JOUEUSE(Classe.ECAFLIP, 110, SpellCharac.TERRE),
	GRIFFE_DE_CEANGAL(Classe.ECAFLIP, 112, SpellCharac.TERRE),
	REKOP(Classe.ECAFLIP, 114, SpellCharac.FEU, SpellCharac.TERRE, SpellCharac.AIR, SpellCharac.EAU),
	DESTIN_ECAFLIP(Classe.ECAFLIP, 120, SpellCharac.TERRE),
	INVOCATION_DE_DOPEUL_ECAFLIP(Classe.ECAFLIP, 1906, SpellCharac.INVOCATION),

	MOT_INTERDIT(Classe.ENIRIPSA, 125, SpellCharac.FEU),
	MOT_DE_FRAYEUR(Classe.ENIRIPSA, 128, SpellCharac.BUFF),
	MOT_CURATIF(Classe.ENIRIPSA, 121, SpellCharac.HEAL),
	MOT_SOIGNANT(Classe.ENIRIPSA, 124, SpellCharac.HEAL),
	MOT_BLESSANT(Classe.ENIRIPSA, 122, SpellCharac.AIR),
	MOT_STIMULANT(Classe.ENIRIPSA, 126, SpellCharac.BUFF),
	MOT_DE_PREVENTION(Classe.ENIRIPSA, 127, SpellCharac.BUFF),
	MOT_DRAINANT(Classe.ENIRIPSA, 123, SpellCharac.BUFF),
	MOT_REVITALISANT(Classe.ENIRIPSA, 130, SpellCharac.HEAL),
	MOT_DE_REGENERATION(Classe.ENIRIPSA, 131, SpellCharac.HEAL),
	MOT_EPINE(Classe.ENIRIPSA, 132, SpellCharac.BUFF),
	MOT_DE_JOUVENCE(Classe.ENIRIPSA, 133, SpellCharac.BUFF),
	MOT_VAMPIRIQUE(Classe.ENIRIPSA, 134, SpellCharac.EAU),
	MOT_DE_SACRIFICE(Classe.ENIRIPSA, 135, SpellCharac.BUFF),
	MOT_AMITIE(Classe.ENIRIPSA, 129, SpellCharac.INVOCATION),
	MOT_IMMOBILISATION(Classe.ENIRIPSA, 136, SpellCharac.BUFF),
	MOT_ENVOL(Classe.ENIRIPSA, 137, SpellCharac.BUFF),
	MOT_DE_SILENCE(Classe.ENIRIPSA, 138, SpellCharac.BUFF),
	MOT_ALTRUISME(Classe.ENIRIPSA, 139, SpellCharac.HEAL),
	MOT_DE_RECONSTITUTION(Classe.ENIRIPSA, 140, SpellCharac.HEAL),
	INVOCATION_DE_DOPEUL_ENIRIPSA(Classe.ENIRIPSA, 1907, SpellCharac.INVOCATION, SpellCharac.HEAL),

	BOND(Classe.IOP, 142, SpellCharac.BUFF),
	INTIMIDATION(Classe.IOP, 143, SpellCharac.NEUTRE),
	PRESSION(Classe.IOP, 141, SpellCharac.TERRE),
	COMPULSION(Classe.IOP, 144, SpellCharac.BUFF),
	EPEE_DIVINE(Classe.IOP, 145, SpellCharac.AIR),
	EPEE_DU_DESTIN(Classe.IOP, 146, SpellCharac.FEU),
	GUIDE_DE_BRAVOURE(Classe.IOP, 147, SpellCharac.BUFF),
	AMPLIFICATION(Classe.IOP, 148, SpellCharac.BUFF),
	EPEE_DESTRUCTRICE(Classe.IOP, 154, SpellCharac.FEU),
	COUPER(Classe.IOP, 150, SpellCharac.FEU),
	SOUFFLE(Classe.IOP, 151, SpellCharac.BUFF),
	VITALITE(Classe.IOP, 155, SpellCharac.BUFF),
	EPEE_DU_JUGEMENT(Classe.IOP, 152, SpellCharac.AIR, SpellCharac.EAU, SpellCharac.FEU),
	PUISSANCE(Classe.IOP, 153, SpellCharac.BUFF),
	MUTILATION(Classe.IOP, 149, SpellCharac.BUFF),
	TEMPETE_DE_PUISSANCE(Classe.IOP, 156, SpellCharac.FEU),
	EPEE_CELESTE(Classe.IOP, 157, SpellCharac.AIR),
	CONCENTRATION(Classe.IOP, 158, SpellCharac.BUFF),
	EPEE_DE_IOP(Classe.IOP, 160, SpellCharac.TERRE),
	COLERE_DE_IOP(Classe.IOP, 159, SpellCharac.TERRE),
	INVOCATION_DE_DOPEUL_IOP(Classe.IOP, 1908, SpellCharac.INVOCATION),

	FLECHE_DE_RECUL(Classe.CRA, 169, SpellCharac.AIR),
	FLECHE_EMPOISONNEE(Classe.CRA, 164, SpellCharac.NEUTRE),
	FLECHE_MAGIQUE(Classe.CRA, 161, SpellCharac.FEU),
	FLECHE_GLACEE(Classe.CRA, 163, SpellCharac.FEU),
	FLECHE_ENFLAMMEE(Classe.CRA, 165, SpellCharac.FEU),
	TIR_ELOIGNEE(Classe.CRA, 172, SpellCharac.BUFF),
	FLECHE_EXPIATION(Classe.CRA, 167, SpellCharac.EAU),
	OEIL_DE_TAUPE(Classe.CRA, 168, SpellCharac.EAU),
	TIR_CRITIQUE(Classe.CRA, 162, SpellCharac.BUFF),
	FLECHE_IMMOBILISATION(Classe.CRA, 170, SpellCharac.AIR),
	FLECHE_PUNITIVE(Classe.CRA, 171, SpellCharac.TERRE),
	TIR_PUISSANT(Classe.CRA, 166, SpellCharac.BUFF),
	FLECHE_HARCELANTE(Classe.CRA, 173, SpellCharac.AIR),
	FLECHE_CINGLANTE(Classe.CRA, 174, SpellCharac.TERRE),
	FLECHE_PERSECUTRICE(Classe.CRA, 176, SpellCharac.FEU, SpellCharac.AIR),
	FLECHE_DESTRUCTRICE(Classe.CRA, 175, SpellCharac.TERRE),
	FLECHE_ABSORBANTE(Classe.CRA, 178, SpellCharac.AIR),
	FLECHE_RALENTISSANTE(Classe.CRA, 177, SpellCharac.EAU),
	FLECHE_EXPLOSIVE(Classe.CRA, 179, SpellCharac.FEU),
	MAITRISE_ARC(Classe.CRA, 180, SpellCharac.BUFF),
	INVOCATION_DE_DOPEUL_CRA(Classe.CRA, 1909, SpellCharac.INVOCATION),

	ATTAQUE_NATURELLE(Classe.FECA, 3, SpellCharac.FEU),
	GLYPHE_AGRESSIF(Classe.FECA, 17, SpellCharac.FEU),
	ARMURE_TERRESTRE(Classe.FECA, 6, SpellCharac.BUFF),
	RENVOI_DE_SORT(Classe.FECA, 4, SpellCharac.BUFF),
	AVEUGLEMENT(Classe.FECA, 2, SpellCharac.BUFF),
	ARMURE_INCANDESCENTE(Classe.FECA, 1, SpellCharac.BUFF),
	ATTAQUE_NUAGEUSE(Classe.FECA, 9, SpellCharac.FEU),
	ARMURE_AQUEUSE(Classe.FECA, 18, SpellCharac.BUFF),
	IMMUNITE(Classe.FECA, 20, SpellCharac.BUFF),
	ARMURE_VENTEUSE(Classe.FECA, 14, SpellCharac.BUFF),
	BULLE(Classe.FECA, 19, SpellCharac.EAU),
	TREVE(Classe.FECA, 5, SpellCharac.BUFF),
	SCIENCE_DU_BATON(Classe.FECA, 16, SpellCharac.BUFF),
	RETOUR_DU_BATON(Classe.FECA, 8, SpellCharac.NEUTRE),
	GLYPHE_AVEUGLEMENT(Classe.FECA, 12, SpellCharac.BUFF),
	TELEPORTATION_FECA(Classe.FECA, 11, SpellCharac.BUFF),
	GLYPHE_ENFLAMME(Classe.FECA, 10, SpellCharac.FEU),
	BOUCLIER_FECA(Classe.FECA, 7, SpellCharac.BUFF),
	GLYPHE_IMMOBILISATION(Classe.FECA, 15, SpellCharac.BUFF),
	GLYPHE_DE_SILENCE(Classe.FECA, 13, SpellCharac.BUFF),
	INVOCATION_DE_DOPEUL_FECA(Classe.FECA, 1901, SpellCharac.INVOCATION),

	CHATIMENT_FORCE(Classe.SACRIEUR, 431, SpellCharac.BUFF),
	PIED_DU_SACRIEUR(Classe.SACRIEUR, 432, SpellCharac.TERRE),
	ATTIRANCE(Classe.SACRIEUR, 434, SpellCharac.BUFF),
	DEROBADE(Classe.SACRIEUR, 444, SpellCharac.BUFF),
	DETOUR(Classe.SACRIEUR, 449, SpellCharac.BUFF),
	ASSAUT(Classe.SACRIEUR, 436, SpellCharac.AIR),
	CHATIMENT_AGILE(Classe.SACRIEUR, 437, SpellCharac.BUFF),
	DISSOLUTION(Classe.SACRIEUR, 439, SpellCharac.EAU),
	CHATIMENT_OSE(Classe.SACRIEUR, 433, SpellCharac.BUFF),
	CHATIMENT_SPIRITUEL(Classe.SACRIEUR, 443, SpellCharac.BUFF),
	SACRIFICE(Classe.SACRIEUR, 440, SpellCharac.BUFF),
	ABSORPTION(Classe.SACRIEUR, 442, SpellCharac.FEU),
	CHATIMENT_VITALESQUE(Classe.SACRIEUR, 441, SpellCharac.BUFF),
	COOPERATION(Classe.SACRIEUR, 445, SpellCharac.BUFF),
	TRANSPOSITION(Classe.SACRIEUR, 438, SpellCharac.BUFF),
	PUNITION(Classe.SACRIEUR, 446, SpellCharac.NEUTRE),
	FURIE(Classe.SACRIEUR, 447, SpellCharac.EAU),
	EPEE_VOLANTE(Classe.SACRIEUR, 448, SpellCharac.INVOCATION),
	TRANSFERT_DE_VIE(Classe.SACRIEUR, 435, SpellCharac.BUFF, SpellCharac.HEAL),
	FOLIE_SANGUINAIRE(Classe.SACRIEUR, 450, SpellCharac.BUFF),
	INVOCATION_DE_DOPEUL_SACRIEUR(Classe.SACRIEUR, 1911, SpellCharac.INVOCATION);

	private Classe classe;
	private int id;
	private SpellCharac[] type;

	private Spells(Classe classe, int id, SpellCharac... type) {
		this.classe = classe;
		this.id = id;
		this.type = type;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the classe
	 */
	public Classe getClasse() {
		return classe;
	}

	public Spell get(Perso perso) {
		return perso.getSpells().get(this);
	}

	public static enum SpellCharac {
		TERRE,
		AIR,
		FEU,
		EAU,
		NEUTRE,
		INVOCATION,
		BUFF,
		HEAL
	}

}
