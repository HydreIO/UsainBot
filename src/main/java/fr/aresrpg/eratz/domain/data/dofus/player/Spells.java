/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.data.dofus.player;

import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.object.Spell;

/**
 * 
 * @since
 */
public enum Spells {

	LA_BLOQUEUSE(1, Classe.SADIDA, 193, new int[] { 1 }, SpellCharac.INVOCATION),
	RONCE(1, Classe.SADIDA, 183, new int[] { 1 }, SpellCharac.TERRE),
	POISON_PARALYSANT(1, Classe.SADIDA, 200, new int[] { 1 }, SpellCharac.FEU),
	SACRIFICE_POUPESQUE(3, Classe.SADIDA, 198, new int[] { 1 }, SpellCharac.BUFF),
	LARME(6, Classe.SADIDA, 195, new int[] { 1 }, SpellCharac.EAU),
	LA_FOLLE(9, Classe.SADIDA, 182, new int[] { 1 }, SpellCharac.INVOCATION),
	RONCE_APAISANTE(13, Classe.SADIDA, 192, new int[] { 1 }, SpellCharac.BUFF),
	PUISSANCE_SYLVESTRE(17, Classe.SADIDA, 197, new int[] { 1 }, SpellCharac.BUFF),
	LA_SACRIFIEE(21, Classe.SADIDA, 189, new int[] { 1 }, SpellCharac.INVOCATION),
	TREMBLEMENT(26, Classe.SADIDA, 181, new int[] { 1 }, SpellCharac.FEU),
	CONNAISSANCE_DES_POUPEES(31, Classe.SADIDA, 199, new int[] { 1 }, SpellCharac.BUFF),
	RONCE_MULTIPLE(36, Classe.SADIDA, 191, new int[] { 1 }, SpellCharac.TERRE),
	ARBRE(42, Classe.SADIDA, 186, new int[] { 1 }, SpellCharac.INVOCATION),
	VENT_EMPOISONE(48, Classe.SADIDA, 196, new int[] { 1 }, SpellCharac.NEUTRE),
	LA_GONFLABLE(54, Classe.SADIDA, 190, new int[] { 1 }, SpellCharac.INVOCATION, SpellCharac.HEAL),
	RONCE_AGRESSIVE(60, Classe.SADIDA, 194, new int[] { 1 }, SpellCharac.TERRE),
	HERBE_FOLLE(70, Classe.SADIDA, 185, new int[] { 1 }, SpellCharac.FEU),
	FEU_DE_BROUSSE(80, Classe.SADIDA, 184, new int[] { 1 }, SpellCharac.FEU, SpellCharac.EAU),
	RONCE_INSOLENTE(90, Classe.SADIDA, 188, new int[] { 1 }, SpellCharac.BUFF),
	LA_SURPUISSANTE(100, Classe.SADIDA, 187, new int[] { 1 }, SpellCharac.INVOCATION),
	DOPEUL_SADIDA(200, Classe.SADIDA, 1910, new int[] { 1 }, SpellCharac.INVOCATION),

	CRI_OURS(1, Classe.OSAMODAS, 23, new int[] { 1 }, SpellCharac.BUFF),
	INVOCATION_TOFU(1, Classe.OSAMODAS, 34, new int[] { 1 }, SpellCharac.INVOCATION),
	GRIFFE_SPECTRALE(1, Classe.OSAMODAS, 21, new int[] { 1 }, SpellCharac.FEU),
	BENEDICTION_ANIMALE(3, Classe.OSAMODAS, 26, new int[] { 1 }, SpellCharac.BUFF),
	DEPLACEMENT_FELIN(6, Classe.OSAMODAS, 22, new int[] { 1 }, SpellCharac.BUFF),
	INVOCATION_DE_BOUFTOU(9, Classe.OSAMODAS, 35, new int[] { 1 }, SpellCharac.INVOCATION),
	CRAPAUD(13, Classe.OSAMODAS, 28, new int[] { 1 }, SpellCharac.BUFF),
	INVOCATION_DE_PRESPIC(17, Classe.OSAMODAS, 37, new int[] { 1 }, SpellCharac.INVOCATION),
	FOUET(21, Classe.OSAMODAS, 30, new int[] { 1 }, SpellCharac.NEUTRE),
	PIQURE_MOTIVANE(26, Classe.OSAMODAS, 27, new int[] { 1 }, SpellCharac.BUFF),
	CORBEAU(31, Classe.OSAMODAS, 24, new int[] { 1 }, SpellCharac.FEU),
	GRIFFE_CINGLANTE(36, Classe.OSAMODAS, 33, new int[] { 1 }, SpellCharac.EAU),
	SOIN_ANIMAL(42, Classe.OSAMODAS, 25, new int[] { 1 }, SpellCharac.HEAL),
	INVOCATION_DE_SANGLIER(48, Classe.OSAMODAS, 38, new int[] { 1 }, SpellCharac.INVOCATION),
	FRAPPE_DU_CRAQUELEUR(54, Classe.OSAMODAS, 36, new int[] { 1 }, SpellCharac.FEU),
	RESISTANCE_NATURELLE(60, Classe.OSAMODAS, 32, new int[] { 1 }, SpellCharac.BUFF),
	CROC_DU_MULOU(70, Classe.OSAMODAS, 29, new int[] { 1 }, SpellCharac.BUFF),
	INVOCATION_DE_BWORK_MAGE(80, Classe.OSAMODAS, 39, new int[] { 1 }, SpellCharac.INVOCATION),
	INVOCATION_DE_CRAQUELEUR(90, Classe.OSAMODAS, 40, new int[] { 1 }, SpellCharac.INVOCATION),
	INVOCATION_DE_DRAGONNET_ROUGE(100, Classe.OSAMODAS, 31, new int[] { 1 }, SpellCharac.INVOCATION),
	INVOCATION_DE_DOPEUL_OSAMODAS(200, Classe.OSAMODAS, 1902, new int[] { 1 }, SpellCharac.INVOCATION),

	LANCER_DE_PIECE(1, Classe.ENUTROF, 51, new int[] { 1 }, SpellCharac.EAU),
	SAC_ANIME(1, Classe.ENUTROF, 41, new int[] { 1 }, SpellCharac.INVOCATION),
	LANCER_DE_PELLE(1, Classe.ENUTROF, 43, new int[] { 1 }, SpellCharac.TERRE),
	PELLE_FANTOMATIQUE(3, Classe.ENUTROF, 49, new int[] { 1 }, SpellCharac.FEU),
	CHANCE(6, Classe.ENUTROF, 42, new int[] { 1 }, SpellCharac.BUFF),
	BOITE_DE_PANDORE(9, Classe.ENUTROF, 47, new int[] { 1 }, SpellCharac.BUFF, SpellCharac.HEAL),
	REMBLAI(13, Classe.ENUTROF, 48, new int[] { 1 }, SpellCharac.TERRE),
	CLE_REDUCTRICE(17, Classe.ENUTROF, 45, new int[] { 1 }, SpellCharac.BUFF),
	FORCE_AGE(21, Classe.ENUTROF, 53, new int[] { 1 }, SpellCharac.AIR),
	DESINVOCATION(26, Classe.ENUTROF, 46, new int[] { 1 }, SpellCharac.AIR),
	CUPIDITE(31, Classe.ENUTROF, 52, new int[] { 1 }, SpellCharac.BUFF),
	ROULAGE_DE_PELLE(36, Classe.ENUTROF, 44, new int[] { 1 }, SpellCharac.TERRE),
	MALADRESSE(42, Classe.ENUTROF, 50, new int[] { 1 }, SpellCharac.BUFF),
	MALADRESSE_DE_MASSE(48, Classe.ENUTROF, 54, new int[] { 1 }, SpellCharac.BUFF),
	ACCELERATION(54, Classe.ENUTROF, 55, new int[] { 1 }, SpellCharac.BUFF),
	PELLE_DU_JUGEMENT(60, Classe.ENUTROF, 56, new int[] { 1 }, SpellCharac.EAU),
	PELLE_MASSACRANTE(70, Classe.ENUTROF, 58, new int[] { 1 }, SpellCharac.EAU),
	CORRUPTION(80, Classe.ENUTROF, 59, new int[] { 1 }, SpellCharac.BUFF),
	PELLE_ANIME(90, Classe.ENUTROF, 57, new int[] { 1 }, SpellCharac.INVOCATION),
	COFFRE_ANIME(100, Classe.ENUTROF, 60, new int[] { 1 }, SpellCharac.INVOCATION),
	INVOCATION_DE_DOPEUL_ENUTROF(200, Classe.ENUTROF, 1903, new int[] { 1 }, SpellCharac.INVOCATION),

	PIEGE_SOURNOIS(1, Classe.SRAM, 65, new int[] { 1 }, SpellCharac.TERRE),
	INVISIBILITE(1, Classe.SRAM, 72, new int[] { 1 }, SpellCharac.BUFF),
	SOURNOISERIE(1, Classe.SRAM, 61, new int[] { 1 }, SpellCharac.TERRE),
	POISON_INSIDIEUX(3, Classe.SRAM, 66, new int[] { 1 }, SpellCharac.AIR),
	FOURVOIEMENT(6, Classe.SRAM, 68, new int[] { 1 }, SpellCharac.AIR),
	COUP_SOURNOIS(9, Classe.SRAM, 63, new int[] { 1 }, SpellCharac.TERRE),
	DOUBLE(13, Classe.SRAM, 74, new int[] { 1 }, SpellCharac.INVOCATION),
	REPERAGE(17, Classe.SRAM, 64, new int[] { 1 }, SpellCharac.BUFF),
	PIEGE_DE_MASSE(21, Classe.SRAM, 79, new int[] { 1 }, SpellCharac.TERRE),
	INVISIBILITE_AUTRUI(26, Classe.SRAM, 78, new int[] { 1 }, SpellCharac.BUFF),
	PIEGE_EMPOISONNE(31, Classe.SRAM, 71, new int[] { 1 }, SpellCharac.TERRE),
	CONCENTRATION_DE_CHAKRA(36, Classe.SRAM, 62, new int[] { 1 }, SpellCharac.BUFF),
	PIEGE_IMMO(42, Classe.SRAM, 69, new int[] { 1 }, SpellCharac.BUFF),
	PIEGE_DE_SILENCE(48, Classe.SRAM, 77, new int[] { 1 }, SpellCharac.BUFF),
	PIEGE_REPULSIF(54, Classe.SRAM, 73, new int[] { 1 }, SpellCharac.BUFF),
	PEUR(60, Classe.SRAM, 67, new int[] { 1 }, SpellCharac.BUFF),
	ARNAQUE(70, Classe.SRAM, 70, new int[] { 1 }, SpellCharac.AIR),
	PULSION_DE_CHAKRA(80, Classe.SRAM, 75, new int[] { 1 }, SpellCharac.BUFF),
	ATTAQUE_MORTELLE(90, Classe.SRAM, 76, new int[] { 1 }, SpellCharac.TERRE),
	PIEGE_MORTEL(100, Classe.SRAM, 79, new int[] { 1 }, SpellCharac.TERRE),
	INVOCATION_DE_DOPEUL_SRAM(200, Classe.SRAM, 1904, new int[] { 1 }, SpellCharac.INVOCATION),

	RALENTISSEMENT(1, Classe.XELOR, 81, new int[] { 1 }, SpellCharac.BUFF),
	AIGUILLE(1, Classe.XELOR, 83, new int[] { 1 }, SpellCharac.INVOCATION),
	CONTRE(1, Classe.XELOR, 82, new int[] { 1 }, SpellCharac.BUFF),
	GELURE(3, Classe.XELOR, 84, new int[] { 1 }, SpellCharac.AIR),
	SABLIER_DE_XELOR(6, Classe.XELOR, 100, new int[] { 1 }, SpellCharac.FEU),
	RAYON_OBSCUR(9, Classe.XELOR, 92, new int[] { 1 }, SpellCharac.FEU),
	TELEPORTATION_XEL(13, Classe.XELOR, 88, new int[] { 1 }, SpellCharac.INVOCATION),
	FLETRISSEMENT(17, Classe.XELOR, 93, new int[] { 1 }, SpellCharac.AIR),
	FLOU(21, Classe.XELOR, 85, new int[] { 1 }, SpellCharac.BUFF),
	POUSSIERE_TEMPORELLE(26, Classe.XELOR, 96, new int[] { 1 }, SpellCharac.FEU),
	VOL_DU_TEMPS(31, Classe.XELOR, 98, new int[] { 1 }, SpellCharac.BUFF),
	AIGUILLE_CHERCHEUSE(36, Classe.XELOR, 86, new int[] { 1 }, SpellCharac.INVOCATION),
	DEVOUEMENT(42, Classe.XELOR, 89, new int[] { 1 }, SpellCharac.BUFF),
	FUITE(48, Classe.XELOR, 90, new int[] { 1 }, SpellCharac.BUFF),
	DEMOTIVATION(54, Classe.XELOR, 87, new int[] { 1 }, SpellCharac.BUFF),
	PROTECTION_AVEUGLANTE(60, Classe.XELOR, 94, new int[] { 1 }, SpellCharac.BUFF),
	MOMIFICATION(70, Classe.XELOR, 99, new int[] { 1 }, SpellCharac.BUFF),
	HORLOGE(80, Classe.XELOR, 95, new int[] { 1 }, SpellCharac.EAU),
	FRAPPE_DE_XELOR(90, Classe.XELOR, 91, new int[] { 1 }, SpellCharac.TERRE),
	CADRAN_DE_XELOR(100, Classe.XELOR, 97, new int[] { 1 }, SpellCharac.INVOCATION),
	INVOCATION_DE_DOPEUL_XELOR(200, Classe.XELOR, 1905, new int[] { 1 }, SpellCharac.INVOCATION),

	PICOLE(1, Classe.PANDAWA, 686, new int[] { 1 }, SpellCharac.BUFF),
	GEULE_DE_BOIS(1, Classe.PANDAWA, 692, new int[] { 1 }, SpellCharac.TERRE),
	POING_ENFLAMME(1, Classe.PANDAWA, 687, new int[] { 1 }, SpellCharac.FEU),
	EPOUVANTE(3, Classe.PANDAWA, 689, new int[] { 1 }, SpellCharac.BUFF),
	SOUFFLE_ALCOOLISE(6, Classe.PANDAWA, 690, new int[] { 1 }, SpellCharac.AIR),
	VULNERABILITE_AQUEUSE(9, Classe.PANDAWA, 691, new int[] { 1 }, SpellCharac.BUFF),
	VULNERABILITE_INCANDESCENTE(13, Classe.PANDAWA, 688, new int[] { 1 }, SpellCharac.BUFF),
	KARCHAM(17, Classe.PANDAWA, 693, new int[] { 1 }, SpellCharac.BUFF),
	VULNERABILITE_VENTEUSE(21, Classe.PANDAWA, 694, new int[] { 1 }, SpellCharac.BUFF),
	STABILISATION(26, Classe.PANDAWA, 695, new int[] { 1 }, SpellCharac.BUFF),
	CHAMRAK(31, Classe.PANDAWA, 696, new int[] { 1 }, SpellCharac.BUFF),
	VULNERABILITE_TERRESTE(36, Classe.PANDAWA, 697, new int[] { 1 }, SpellCharac.BUFF),
	SOUILLURE(42, Classe.PANDAWA, 698, new int[] { 1 }, SpellCharac.BUFF),
	LAIT_DE_BAMBOU(48, Classe.PANDAWA, 699, new int[] { 1 }, SpellCharac.BUFF),
	VAGUE_A_LAME(54, Classe.PANDAWA, 700, new int[] { 1 }, SpellCharac.EAU),
	COLERE_DE_ZATOISHWAN(60, Classe.PANDAWA, 701, new int[] { 1 }, SpellCharac.BUFF),
	FLASQUE_EXPLOSIVE(70, Classe.PANDAWA, 702, new int[] { 1 }, SpellCharac.FEU),
	PANDATAK(80, Classe.PANDAWA, 703, new int[] { 1 }, SpellCharac.TERRE),
	PANDANLKU(90, Classe.PANDAWA, 704, new int[] { 1 }, SpellCharac.BUFF),
	LIEN_SPIRITUEUX(100, Classe.PANDAWA, 705, new int[] { 1 }, SpellCharac.INVOCATION),
	INVOCATION_DE_DOPEUL_PANDAWA(200, Classe.PANDAWA, 1912, new int[] { 1 }, SpellCharac.INVOCATION),

	PILE_OU_FACE(1, Classe.ECAFLIP, 102, new int[] { 1 }, SpellCharac.TERRE),
	CHANCE_ECAFLIP(1, Classe.ECAFLIP, 103, new int[] { 1 }, SpellCharac.BUFF, SpellCharac.HEAL),
	BOND_DU_FELIN(1, Classe.ECAFLIP, 105, new int[] { 1 }, SpellCharac.BUFF),
	BLUFF(3, Classe.ECAFLIP, 109, new int[] { 1 }, SpellCharac.AIR, SpellCharac.EAU),
	PERCEPTION(6, Classe.ECAFLIP, 113, new int[] { 1 }, SpellCharac.BUFF),
	CONTRECOUP(9, Classe.ECAFLIP, 111, new int[] { 1 }, SpellCharac.BUFF),
	TREFLE(13, Classe.ECAFLIP, 104, new int[] { 1 }, SpellCharac.BUFF),
	TOUT_OU_RIEN(17, Classe.ECAFLIP, 119, new int[] { 1 }, SpellCharac.NEUTRE, SpellCharac.HEAL),
	ROULETTE(21, Classe.ECAFLIP, 101, new int[] { 1 }, SpellCharac.BUFF),
	TOPKAJ(26, Classe.ECAFLIP, 107, new int[] { 1 }, SpellCharac.FEU),
	LANGUE_RAPEUSE(31, Classe.ECAFLIP, 116, new int[] { 1 }, SpellCharac.FEU),
	ROUE_DE_LA_FORTUNE(36, Classe.ECAFLIP, 106, new int[] { 1 }, SpellCharac.BUFF),
	GRIFFE_INVOCATRICE(42, Classe.ECAFLIP, 117, new int[] { 1 }, SpellCharac.INVOCATION),
	ESPRIT_FELIN(48, Classe.ECAFLIP, 108, new int[] { 1 }, SpellCharac.TERRE),
	ODORAT(54, Classe.ECAFLIP, 115, new int[] { 1 }, SpellCharac.BUFF),
	REFLEXE(60, Classe.ECAFLIP, 118, new int[] { 1 }, SpellCharac.BUFF),
	GRIFFE_JOUEUSE(70, Classe.ECAFLIP, 110, new int[] { 1 }, SpellCharac.TERRE),
	GRIFFE_DE_CEANGAL(80, Classe.ECAFLIP, 112, new int[] { 1 }, SpellCharac.TERRE),
	REKOP(90, Classe.ECAFLIP, 114, new int[] { 1 }, SpellCharac.FEU, SpellCharac.TERRE, SpellCharac.AIR, SpellCharac.EAU),
	DESTIN_ECAFLIP(100, Classe.ECAFLIP, 120, new int[] { 1 }, SpellCharac.TERRE),
	INVOCATION_DE_DOPEUL_ECAFLIP(200, Classe.ECAFLIP, 1906, new int[] { 1 }, SpellCharac.INVOCATION),

	MOT_INTERDIT(1, Classe.ENIRIPSA, 125, new int[] { 1 }, SpellCharac.FEU),
	MOT_DE_FRAYEUR(1, Classe.ENIRIPSA, 128, new int[] { 1 }, SpellCharac.BUFF),
	MOT_CURATIF(1, Classe.ENIRIPSA, 121, new int[] { 1 }, SpellCharac.HEAL),
	MOT_SOIGNANT(3, Classe.ENIRIPSA, 124, new int[] { 1 }, SpellCharac.HEAL),
	MOT_BLESSANT(6, Classe.ENIRIPSA, 122, new int[] { 1 }, SpellCharac.AIR),
	MOT_STIMULANT(9, Classe.ENIRIPSA, 126, new int[] { 1 }, SpellCharac.BUFF),
	MOT_DE_PREVENTION(13, Classe.ENIRIPSA, 127, new int[] { 1 }, SpellCharac.BUFF),
	MOT_DRAINANT(17, Classe.ENIRIPSA, 123, new int[] { 1 }, SpellCharac.BUFF),
	MOT_REVITALISANT(21, Classe.ENIRIPSA, 130, new int[] { 1 }, SpellCharac.HEAL),
	MOT_DE_REGENERATION(26, Classe.ENIRIPSA, 131, new int[] { 1 }, SpellCharac.HEAL),
	MOT_EPINE(31, Classe.ENIRIPSA, 132, new int[] { 1 }, SpellCharac.BUFF),
	MOT_DE_JOUVENCE(36, Classe.ENIRIPSA, 133, new int[] { 1 }, SpellCharac.BUFF),
	MOT_VAMPIRIQUE(42, Classe.ENIRIPSA, 134, new int[] { 1 }, SpellCharac.EAU),
	MOT_DE_SACRIFICE(48, Classe.ENIRIPSA, 135, new int[] { 1 }, SpellCharac.BUFF),
	MOT_AMITIE(54, Classe.ENIRIPSA, 129, new int[] { 1 }, SpellCharac.INVOCATION),
	MOT_IMMOBILISATION(60, Classe.ENIRIPSA, 136, new int[] { 1 }, SpellCharac.BUFF),
	MOT_ENVOL(70, Classe.ENIRIPSA, 137, new int[] { 1 }, SpellCharac.BUFF),
	MOT_DE_SILENCE(80, Classe.ENIRIPSA, 138, new int[] { 1 }, SpellCharac.BUFF),
	MOT_ALTRUISME(90, Classe.ENIRIPSA, 139, new int[] { 1 }, SpellCharac.HEAL),
	MOT_DE_RECONSTITUTION(100, Classe.ENIRIPSA, 140, new int[] { 1 }, SpellCharac.HEAL),
	INVOCATION_DE_DOPEUL_ENIRIPSA(200, Classe.ENIRIPSA, 1907, new int[] { 1 }, SpellCharac.INVOCATION, SpellCharac.HEAL),

	BOND(1, Classe.IOP, 142, new int[] { 1 }, SpellCharac.BUFF),
	INTIMIDATION(1, Classe.IOP, 143, new int[] { 1 }, SpellCharac.NEUTRE),
	PRESSION(1, Classe.IOP, 141, new int[] { 1 }, SpellCharac.TERRE),
	COMPULSION(3, Classe.IOP, 144, new int[] { 1 }, SpellCharac.BUFF),
	EPEE_DIVINE(6, Classe.IOP, 145, new int[] { 1 }, SpellCharac.AIR),
	EPEE_DU_DESTIN(9, Classe.IOP, 146, new int[] { 1 }, SpellCharac.FEU),
	GUIDE_DE_BRAVOURE(13, Classe.IOP, 147, new int[] { 1 }, SpellCharac.BUFF),
	AMPLIFICATION(17, Classe.IOP, 148, new int[] { 1 }, SpellCharac.BUFF),
	EPEE_DESTRUCTRICE(21, Classe.IOP, 154, new int[] { 1 }, SpellCharac.FEU),
	COUPER(26, Classe.IOP, 150, new int[] { 1 }, SpellCharac.FEU),
	SOUFFLE(31, Classe.IOP, 151, new int[] { 1 }, SpellCharac.BUFF),
	VITALITE(36, Classe.IOP, 155, new int[] { 1 }, SpellCharac.BUFF),
	EPEE_DU_JUGEMENT(42, Classe.IOP, 152, new int[] { 1 }, SpellCharac.AIR, SpellCharac.EAU, SpellCharac.FEU),
	PUISSANCE(48, Classe.IOP, 153, new int[] { 1 }, SpellCharac.BUFF),
	MUTILATION(54, Classe.IOP, 149, new int[] { 1 }, SpellCharac.BUFF),
	TEMPETE_DE_PUISSANCE(60, Classe.IOP, 156, new int[] { 1 }, SpellCharac.FEU),
	EPEE_CELESTE(70, Classe.IOP, 157, new int[] { 1 }, SpellCharac.AIR),
	CONCENTRATION(80, Classe.IOP, 158, new int[] { 1 }, SpellCharac.BUFF),
	EPEE_DE_IOP(90, Classe.IOP, 160, new int[] { 1 }, SpellCharac.TERRE),
	COLERE_DE_IOP(100, Classe.IOP, 159, new int[] { 1 }, SpellCharac.TERRE),
	INVOCATION_DE_DOPEUL_IOP(200, Classe.IOP, 1908, new int[] { 1 }, SpellCharac.INVOCATION),

	FLECHE_DE_RECUL(1, Classe.CRA, 169, new int[] { 3, 4, 5, 6, 7, 8 }, SpellCharac.AIR),
	FLECHE_EMPOISONNEE(1, Classe.CRA, 164, new int[] { 5, 6, 7, 8, 9, 10 }, SpellCharac.NEUTRE),
	FLECHE_MAGIQUE(1, Classe.CRA, 161, new int[] { 7, 8, 9, 10, 11, 12 }, SpellCharac.FEU),
	FLECHE_GLACEE(3, Classe.CRA, 163, new int[] { 5, 6, 7, 8, 9, 10 }, SpellCharac.FEU),
	FLECHE_ENFLAMMEE(6, Classe.CRA, 165, new int[] { 3, 4, 5, 6, 7, 8 }, SpellCharac.FEU),
	TIR_ELOIGNEE(9, Classe.CRA, 172, new int[] { 0, 0, 0, 0, 0, 0 }, SpellCharac.BUFF),
	FLECHE_EXPIATION(13, Classe.CRA, 167, new int[] { 10, 10, 10, 10, 10, 10 }, SpellCharac.EAU),
	OEIL_DE_TAUPE(17, Classe.CRA, 168, new int[] { 5, 6, 7, 8, 9, 10 }, SpellCharac.EAU),
	TIR_CRITIQUE(21, Classe.CRA, 162, new int[] { 1, 2, 3, 4, 5, 6 }, SpellCharac.BUFF),
	FLECHE_IMMOBILISATION(26, Classe.CRA, 170, new int[] { 5, 6, 7, 8, 9, 10 }, SpellCharac.AIR),
	FLECHE_PUNITIVE(31, Classe.CRA, 171, new int[] { 8, 8, 8, 8, 8, 8 }, SpellCharac.TERRE),
	TIR_PUISSANT(36, Classe.CRA, 166, new int[] { 1, 2, 3, 4, 5, 6 }, SpellCharac.BUFF),
	FLECHE_HARCELANTE(42, Classe.CRA, 173, new int[] { 7, 8, 9, 10, 11, 12 }, SpellCharac.AIR),
	FLECHE_CINGLANTE(48, Classe.CRA, 174, new int[] { 5, 6, 7, 8, 9, 10 }, SpellCharac.TERRE),
	FLECHE_PERSECUTRICE(54, Classe.CRA, 176, new int[] { 8, 8, 8, 8, 8, 8 }, SpellCharac.FEU, SpellCharac.AIR),
	FLECHE_DESTRUCTRICE(60, Classe.CRA, 175, new int[] { 8, 8, 8, 8, 8, 8 }, SpellCharac.TERRE),
	FLECHE_ABSORBANTE(70, Classe.CRA, 178, new int[] { 8, 8, 8, 8, 8, 8 }, SpellCharac.AIR),
	FLECHE_RALENTISSANTE(80, Classe.CRA, 177, new int[] { 8, 8, 8, 8, 8, 8 }, SpellCharac.EAU),
	FLECHE_EXPLOSIVE(90, Classe.CRA, 179, new int[] { 8, 8, 8, 8, 8, 8 }, SpellCharac.FEU),
	MAITRISE_ARC(100, Classe.CRA, 180, new int[] { 1, 2, 3, 4, 5, 6 }, SpellCharac.BUFF),
	INVOCATION_DE_DOPEUL_CRA(200, Classe.CRA, 1909, new int[] { 1 }, SpellCharac.INVOCATION),

	ATTAQUE_NATURELLE(1, Classe.FECA, 3, new int[] { 1 }, SpellCharac.FEU),
	GLYPHE_AGRESSIF(1, Classe.FECA, 17, new int[] { 1 }, SpellCharac.FEU),
	ARMURE_TERRESTRE(1, Classe.FECA, 6, new int[] { 1 }, SpellCharac.BUFF),
	RENVOI_DE_SORT(3, Classe.FECA, 4, new int[] { 1 }, SpellCharac.BUFF),
	AVEUGLEMENT(6, Classe.FECA, 2, new int[] { 1 }, SpellCharac.BUFF),
	ARMURE_INCANDESCENTE(9, Classe.FECA, 1, new int[] { 1 }, SpellCharac.BUFF),
	ATTAQUE_NUAGEUSE(13, Classe.FECA, 9, new int[] { 1 }, SpellCharac.FEU),
	ARMURE_AQUEUSE(17, Classe.FECA, 18, new int[] { 1 }, SpellCharac.BUFF),
	IMMUNITE(21, Classe.FECA, 20, new int[] { 1 }, SpellCharac.BUFF),
	ARMURE_VENTEUSE(26, Classe.FECA, 14, new int[] { 1 }, SpellCharac.BUFF),
	BULLE(31, Classe.FECA, 19, new int[] { 1 }, SpellCharac.EAU),
	TREVE(36, Classe.FECA, 5, new int[] { 1 }, SpellCharac.BUFF),
	SCIENCE_DU_BATON(42, Classe.FECA, 16, new int[] { 1 }, SpellCharac.BUFF),
	RETOUR_DU_BATON(48, Classe.FECA, 8, new int[] { 1 }, SpellCharac.NEUTRE),
	GLYPHE_AVEUGLEMENT(54, Classe.FECA, 12, new int[] { 1 }, SpellCharac.BUFF),
	TELEPORTATION_FECA(60, Classe.FECA, 11, new int[] { 1 }, SpellCharac.BUFF),
	GLYPHE_ENFLAMME(70, Classe.FECA, 10, new int[] { 1 }, SpellCharac.FEU),
	BOUCLIER_FECA(80, Classe.FECA, 7, new int[] { 1 }, SpellCharac.BUFF),
	GLYPHE_IMMOBILISATION(90, Classe.FECA, 15, new int[] { 1 }, SpellCharac.BUFF),
	GLYPHE_DE_SILENCE(100, Classe.FECA, 13, new int[] { 1 }, SpellCharac.BUFF),
	INVOCATION_DE_DOPEUL_FECA(200, Classe.FECA, 1901, new int[] { 1 }, SpellCharac.INVOCATION),

	CHATIMENT_FORCE(1, Classe.SACRIEUR, 431, new int[] { 1 }, SpellCharac.BUFF),
	PIED_DU_SACRIEUR(1, Classe.SACRIEUR, 432, new int[] { 1 }, SpellCharac.TERRE),
	ATTIRANCE(1, Classe.SACRIEUR, 434, new int[] { 1 }, SpellCharac.BUFF),
	DEROBADE(3, Classe.SACRIEUR, 444, new int[] { 1 }, SpellCharac.BUFF),
	DETOUR(6, Classe.SACRIEUR, 449, new int[] { 1 }, SpellCharac.BUFF),
	ASSAUT(9, Classe.SACRIEUR, 436, new int[] { 1 }, SpellCharac.AIR),
	CHATIMENT_AGILE(13, Classe.SACRIEUR, 437, new int[] { 1 }, SpellCharac.BUFF),
	DISSOLUTION(17, Classe.SACRIEUR, 439, new int[] { 1 }, SpellCharac.EAU),
	CHATIMENT_OSE(21, Classe.SACRIEUR, 433, new int[] { 1 }, SpellCharac.BUFF),
	CHATIMENT_SPIRITUEL(26, Classe.SACRIEUR, 443, new int[] { 1 }, SpellCharac.BUFF),
	SACRIFICE(31, Classe.SACRIEUR, 440, new int[] { 1 }, SpellCharac.BUFF),
	ABSORPTION(36, Classe.SACRIEUR, 442, new int[] { 1 }, SpellCharac.FEU),
	CHATIMENT_VITALESQUE(42, Classe.SACRIEUR, 441, new int[] { 1 }, SpellCharac.BUFF),
	COOPERATION(48, Classe.SACRIEUR, 445, new int[] { 1 }, SpellCharac.BUFF),
	TRANSPOSITION(54, Classe.SACRIEUR, 438, new int[] { 1 }, SpellCharac.BUFF),
	PUNITION(60, Classe.SACRIEUR, 446, new int[] { 1 }, SpellCharac.NEUTRE),
	FURIE(70, Classe.SACRIEUR, 447, new int[] { 1 }, SpellCharac.EAU),
	EPEE_VOLANTE(80, Classe.SACRIEUR, 448, new int[] { 1 }, SpellCharac.INVOCATION),
	TRANSFERT_DE_VIE(90, Classe.SACRIEUR, 435, new int[] { 1 }, SpellCharac.BUFF, SpellCharac.HEAL),
	FOLIE_SANGUINAIRE(100, Classe.SACRIEUR, 450, new int[] { 1 }, SpellCharac.BUFF),
	INVOCATION_DE_DOPEUL_SACRIEUR(200, Classe.SACRIEUR, 1911, new int[] { 1 }, SpellCharac.INVOCATION);

	private int lvl;
	private Classe classe;
	private int id;
	private SpellCharac[] type;
	private int[] po;

	private Spells(int lvl, Classe classe, int id, int[] po, SpellCharac... type) {
		this.classe = classe;
		this.id = id;
		this.type = type;
		this.lvl = lvl;
		this.po = po;
	}

	/**
	 * @return the lvl
	 */
	public int getLvl() {
		return lvl;
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
		return perso.getStatsInfos().getSpells().get(this);
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
