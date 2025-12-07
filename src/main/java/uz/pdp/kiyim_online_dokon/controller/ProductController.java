package uz.pdp.kiyim_online_dokon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.kiyim_online_dokon.dto.ProductImageDTO;
import uz.pdp.kiyim_online_dokon.dto.ProductsDTO;
import uz.pdp.kiyim_online_dokon.service.interfaces.ProductsService;


import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductsService productsService;

    @GetMapping
    public ResponseEntity<List<ProductsDTO>> getAllProducts() {
        return ResponseEntity.ok(productsService.getAllProducts());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductsDTO> getProductById(@PathVariable Integer id) {
        ProductsDTO dto = productsService.getProductById(id);
        return ResponseEntity.ok(dto);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductsDTO> createProduct(@RequestBody ProductsDTO dto) {
        ProductsDTO createdProduct = productsService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductsDTO> updateProduct(@PathVariable Integer id,
                                                     @RequestBody ProductsDTO dto) {
        ProductsDTO updatedProduct = productsService.updateProduct(id, dto);
        return ResponseEntity.ok(updatedProduct);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productsService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{productId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductImageDTO> uploadImage(@PathVariable Integer productId,
                                                       @RequestParam("file") MultipartFile file,
                                                       @RequestParam(value = "isMain", defaultValue = "false") boolean isMain) {
        ProductImageDTO uploadedImage = productsService.uploadImage(productId, file, isMain);
        return ResponseEntity.status(HttpStatus.CREATED).body(uploadedImage);
    }


    @GetMapping("/images/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer imageId) {
        ProductImageDTO image = productsService.getImageById(imageId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(image.getImageBytes().length);

        return new ResponseEntity<>(image.getImageBytes(), headers, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Integer imageId) {
        productsService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }


}
