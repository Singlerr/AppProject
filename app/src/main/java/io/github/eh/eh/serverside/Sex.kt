package io.github.eh.eh.serverside

import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.io.Serializable

@JsonTypeInfo(include = JsonTypeInfo.As.EXTERNAL_PROPERTY, use = JsonTypeInfo.Id.NAME)
enum class Sex(val locale: String) : Serializable {
    MALE("남성"), FEMALE("여성");
    object Locale{
        fun getLocale(sex:String) : String{
            return  if(sex == "MALE") "남성" else "여성"
        }
    }
}