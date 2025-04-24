package vn.hoidanit.laptopshop.domain.dto;

import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.elastic.ElasticProduct;

public class ProductElasticDTO {
    // convert từ ElasticProduct sang Product
    private long id;
    private String name;
    private double price;
    private String image;
    private String detailDesc;
    private String shortDesc;
    private long quantity;
    private long sold;
    private String factory;
    private String target;

    // Phương thức chuyển đổi từ ElasticProduct sang Product
    public static Product convertToProduct(ElasticProduct elasticProduct) {
        Product product = new Product();
        product.setId(elasticProduct.getId());
        product.setName(elasticProduct.getName());
        product.setPrice(elasticProduct.getPrice());
        product.setImage(elasticProduct.getImage());
        product.setDetailDesc(elasticProduct.getDetailDesc());
        product.setShortDesc(elasticProduct.getShortDesc());
        product.setQuantity(elasticProduct.getQuantity());
        product.setSold(elasticProduct.getSold());
        product.setFactory(elasticProduct.getFactory());
        product.setTarget(elasticProduct.getTarget());
        return product;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDetailDesc() {
        return detailDesc;
    }

    public void setDetailDesc(String detailDesc) {
        this.detailDesc = detailDesc;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getSold() {
        return sold;
    }

    public void setSold(long sold) {
        this.sold = sold;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

}