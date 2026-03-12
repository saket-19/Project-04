package in.co.rays.proj4.bean;

public class StockBean extends BaseBean {

    private long stockId;
    private String stockName;
    private double price;
    private Integer quantity;

    public long getStockId() {
        return stockId;
    }

    public void setStockId(long stockId) {
        this.stockId = stockId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getKey() {
        return String.valueOf(stockId);
    }

    @Override
    public String getValue() {
        return stockName;
    }
}