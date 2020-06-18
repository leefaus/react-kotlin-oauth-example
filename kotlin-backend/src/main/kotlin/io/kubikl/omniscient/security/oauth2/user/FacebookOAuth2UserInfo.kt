package io.kubikl.omniscient.security.oauth2.user

class FacebookOAuth2UserInfo(attributes: Map<String?, Any?>?) : OAuth2UserInfo(attributes) {
    override val id: String?
        get() = attributes?.get("id") as String?

    override val name: String?
        get() = attributes?.get("name") as String?

    override val email: String?
        get() = attributes?.get("email") as String?

    override val imageUrl: String?
        get() {
            if (attributes?.containsKey("picture")!!) {
                val pictureObj = attributes?.get("picture") as Map<String, Any>?
                if (pictureObj!!.containsKey("data")) {
                    val dataObj = pictureObj["data"] as Map<String, Any>?
                    if (dataObj!!.containsKey("url")) {
                        return dataObj["url"] as String?
                    }
                }
            }
            return null
        }
}