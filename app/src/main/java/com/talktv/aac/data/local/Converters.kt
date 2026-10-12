package com.talktv.aac.data.local

import androidx.room.TypeConverter
import com.talktv.aac.data.model.TileAction
import com.talktv.aac.data.model.TileCategory

class Converters {

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return value?.joinToString("|||") ?: ""
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()
        return value.split("|||")
    }

    @TypeConverter
    fun fromTileCategory(category: TileCategory?): String {
        return category?.name ?: TileCategory.CORE.name
    }

    @TypeConverter
    fun toTileCategory(name: String?): TileCategory {
        return try {
            TileCategory.valueOf(name ?: TileCategory.CORE.name)
        } catch (_: Exception) {
            TileCategory.CORE
        }
    }

    @TypeConverter
    fun fromTileAction(action: TileAction?): String {
        return action?.name ?: TileAction.SPEAK_ONLY.name
    }

    @TypeConverter
    fun toTileAction(name: String?): TileAction {
        return try {
            TileAction.valueOf(name ?: TileAction.SPEAK_ONLY.name)
        } catch (_: Exception) {
            TileAction.SPEAK_ONLY
        }
    }
}
