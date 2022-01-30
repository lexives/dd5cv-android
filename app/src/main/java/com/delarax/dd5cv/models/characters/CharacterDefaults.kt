package com.delarax.dd5cv.models.characters

object CharacterDefaults {
    private val STR = Ability(name = "Strength", abbreviation = "STR")
    private val DEX = Ability(name = "Dexterity", abbreviation = "DEX")
    private val CON = Ability(name = "Constitution", abbreviation = "CON")
    private val INT = Ability(name = "Intelligence", abbreviation = "INT")
    private val WIS = Ability(name = "Wisdom", abbreviation = "WIS")
    private val CHA = Ability(name = "Charisma", abbreviation = "CHA")

    val abilities = listOf(STR, DEX, CON, INT, WIS, CHA)

    val savingThrows = listOf(
        Proficiency(name = STR.name, ability = STR),
        Proficiency(name = DEX.name, ability = DEX),
        Proficiency(name = CON.name, ability = CON),
        Proficiency(name = INT.name, ability = INT),
        Proficiency(name = WIS.name, ability = WIS),
        Proficiency(name = CHA.name, ability = CHA),
    )

    val skills = listOf(
        Proficiency(name = "Acrobatics", ability = DEX),
        Proficiency(name = "Animal Handling", ability = WIS),
        Proficiency(name = "Arcana", ability = INT),
        Proficiency(name = "Athletics", ability = STR),
        Proficiency(name = "Deception", ability = CHA),
        Proficiency(name = "History", ability = INT),
        Proficiency(name = "Insight", ability = WIS),
        Proficiency(name = "Intimidation", ability = CHA),
        Proficiency(name = "Investigation", ability = INT),
        Proficiency(name = "Medicine", ability = WIS),
        Proficiency(name = "Nature", ability = INT),
        Proficiency(name = "Perception", ability = WIS),
        Proficiency(name = "Performance", ability = CHA),
        Proficiency(name = "Persuasion", ability = CHA),
        Proficiency(name = "Religion", ability = INT),
        Proficiency(name = "Sleight of Hand", ability = DEX),
        Proficiency(name = "Stealth", ability = DEX),
        Proficiency(name = "Survival", ability = WIS),
    )
}