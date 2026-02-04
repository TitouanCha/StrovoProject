package com.example.strovo.data

data class Activity(
    val id: Long,
    val resource_state: Int,
    val visibility: String
)



enum class ChartTrend {
    UP,
    DOWN,
    STABLE
}
data class Crypto(
    val id: String,
    val name: String,
    val price: Double,
    val chartTrend: ChartTrend
){
    val formatedPrice: String
        get() = "$${"%.2f".format(price)}"
}

data class Holding(
    val id: String,
    var cryptoName: Crypto,
    var amount: Int
){
    val totalValue: Double
        get() = cryptoName.price * amount
    val formatedTotalValue: String
        get() = "$${"%.2f".format(totalValue)}"
}

data class Wallet(
    var name: String = "Wallet",
    var holdings: List<Holding>
){
    val totalValue: Double
        get() = holdings.sumOf { it.totalValue }
    val cryptoCount: Int
        get() = holdings.size
    val averageValue: Double
        get() = if (holdings.isEmpty()) 0.0 else totalValue / cryptoCount

}