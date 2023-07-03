package net.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseApi {
    private boolean status;  // 1:success 0:error
    private String message; // caso de error: mensaje de error
    private Object data;    // caso de exito: datos

    public ResponseApi(Object data) {
        this.data = data;
    }
}
