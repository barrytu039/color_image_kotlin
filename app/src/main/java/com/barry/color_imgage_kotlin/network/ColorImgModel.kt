package com.barry.color_imgage_kotlin.network

data class ColorImgModel(
    val albumId:Int,
    val id: Int,
    val title: String,
    val url: String?,
    val thumbnailUrl: String) {
    /**
     * albumId : 1
     * id : 1
     * title : accusamus beatae ad facilis cum similique qui sunt
     * url : https://via.placeholder.com/600/92c952
     * thumbnailUrl : https://via.placeholder.com/150/92c952
     */
}
