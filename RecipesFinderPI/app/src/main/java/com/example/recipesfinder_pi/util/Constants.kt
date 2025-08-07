package com.example.recipesfinder.util


import com.example.recipesfinder.R

/**
 * A container for application-wide constants.
 *
 * This class holds constant values and mappings used across the application,
 * such as status codes for database operations and mappings for user profile images and food type images.
 */
class Constants {

    /**
     * Constants representing status messages for database operations.
     *
     * The following values are defined:
     * - [SUCCESS]: Operation completed successfully.
     * - [FAIL]: Operation failed.
     * - [CONSTRAINT]: A constraint error occurred (e.g., duplicate entry).
     * - [NOT_FOUND]: The requested record was not found.
     */
    object BD_MSGS {
        val SUCCESS = 1
        val FAIL = 0
        val CONSTRAINT = 2
        val NOT_FOUND = 3
    }

    /**
     * Constants related to user images and food type images.
     *
     * Provides:
     * - [profileImages]: An array of profile image identifiers as strings.
     * - [profileImageMap]: A mapping from profile image identifiers (strings) to drawable resource IDs.
     * - [foodTypeMap]: A mapping from food type names to drawable resource IDs.
     */
    object USER_IMAGES {
        val profileImages = arrayOf(
            "profile1",
            "profile2",
            "profile3",
            "profile4",
            "profile5",
            "profile6",
            "profile7",
            "profile8",
            "profile9",
            "profile10",
            "profile11",
            "profile12",
            "profile13",
            "profile14",
            "profile15",
            "profile16",
            "profile17",
            "profile18",
            "profile19",
            "profile20"
        )

        val profileImageMap = mapOf(
            "profile1" to R.drawable.profile1,
            "profile2" to R.drawable.profile2,
            "profile3" to R.drawable.profile3,
            "profile4" to R.drawable.profile4,
            "profile5" to R.drawable.profile5,
            "profile6" to R.drawable.profile6,
            "profile7" to R.drawable.profile7,
            "profile8" to R.drawable.profile8,
            "profile9" to R.drawable.profile9,
            "profile10" to R.drawable.profile10,
            "profile11" to R.drawable.profile11,
            "profile12" to R.drawable.profile12,
            "profile13" to R.drawable.profile13,
            "profile14" to R.drawable.profile14,
            "profile15" to R.drawable.profile15,
            "profile16" to R.drawable.profile16,
            "profile17" to R.drawable.profile17,
            "profile18" to R.drawable.profile18,
            "profile19" to R.drawable.profile19,
            "profile20" to R.drawable.profile20
        )

        val foodTypeMap = mapOf(
            "Japonesa" to R.drawable.japonesa,
            "Italiana" to R.drawable.italiana,
            "Mexicana" to R.drawable.mexicana,
            "Brasileira" to R.drawable.brasileira,
            "Chinesa" to R.drawable.chinesa,
            "Indiana" to R.drawable.indiana,
            "Mediterrânea" to R.drawable.mediterranea,
            "Francesa" to R.drawable.francesa,
            "Alemã" to R.drawable.alema,
            "Americana" to R.drawable.lanches,
            "Tailandesa" to R.drawable.tailandesa,
            "Coreana" to R.drawable.coreana,
            "Árabe" to R.drawable.arabe,
            "Espanhola" to R.drawable.espanhola,
            "Vietnamita" to R.drawable.vietnamita,
            "Caribenha" to R.drawable.caribenha,
            "Grega" to R.drawable.grega,
            "Doces" to R.drawable.doces,
            "Vegetariana" to R.drawable.vegetariana,
            "Low Carb" to R.drawable.low_carb
        )
    }
}
