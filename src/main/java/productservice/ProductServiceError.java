package productservice;

import lombok.Data;

@Data
public class ProductServiceError extends Throwable{

    private String message;

    public ProductServiceError(String errorMessage){
        this.message = errorMessage;
    }
}
