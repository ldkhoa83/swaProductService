package productservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ProductController {

    private Map<String,ProductDTO> products = new HashMap<>();

    @Autowired
    private StockFeignClient stockService;

    @GetMapping("/products/{productNumber}")
    public ResponseEntity<?> getProduct(@PathVariable String productNumber){
        if(products.containsKey(productNumber)){
            ProductDTO product = products.get(productNumber);
            product.setNrInStock(stockService.getStockStatus(productNumber));
            return new ResponseEntity<>(product,HttpStatus.OK);
        }else {
            return new ResponseEntity<ProductServiceError>(new ProductServiceError("Invalid product"), HttpStatus.NOT_FOUND);
        }
    }

    @FeignClient(name = "stock-service")
    interface StockFeignClient {

        @GetMapping("/stocks/{productNumber}")
        Integer getStockStatus(@PathVariable String productNumber);
    }

    @PostConstruct
    public void init(){
        products.put("A001",new ProductDTO("A001","iPhone 14",0));
        products.put("B002",new ProductDTO("B002","Samsung ZFold 4",0));
        products.put("C003",new ProductDTO("C003","Pixel 10",0));
    }
}
