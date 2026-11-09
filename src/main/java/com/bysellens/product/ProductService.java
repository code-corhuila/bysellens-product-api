package com.bysellens.product;
import java.util.*;import java.math.BigDecimal;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import org.springframework.beans.factory.annotation.Value;import org.springframework.web.multipart.MultipartFile;import com.bysellens.common.*;
@Service public class ProductService {
 private final ProductoRepository repo;private final Remote remote;private final Images images;private final String inventory;
 public ProductService(ProductoRepository r,Remote remote,Images images,@Value("${inventory.url}")String inventory){repo=r;this.remote=remote;this.images=images;this.inventory=inventory;}
 public record Stock(long productoId,int stock,int stockMinimo,int reservado){}
 public record View(long id,String codigo,String nombre,String descripcion,String categoria,String tono,BigDecimal precioCompra,BigDecimal precioVenta,int stock,int stockMinimo,String imagen,boolean activo){}
 public Producto entity(long id){return repo.findById(id).orElseThrow(DomainException::missing);}
 View view(Producto p){var s=remote.get(inventory+"/internal/inventario/"+p.id,Stock.class);return new View(p.id,p.codigo,p.nombre,p.descripcion,p.categoria,p.tono,p.precioCompra,p.precioVenta,s.stock(),s.stockMinimo(),p.imagen,p.activo);}
 public View get(long id){return view(entity(id));}
 public List<View> list(){return repo.findByActivoTrueOrderByIdAsc().stream().map(this::view).toList();}
 @Transactional public View save(Long id,ProductController.Input input,MultipartFile image){if(input.precioVenta().compareTo(input.precioCompra())<0)throw new DomainException(400,"Precio venta inferior al de compra");if(id==null?repo.existsByCodigo(input.codigo()):repo.existsByCodigoAndIdNot(input.codigo(),id))throw new DomainException(409,"Codigo ya registrado");var p=id==null?new Producto():entity(id);p.codigo=input.codigo().trim();p.nombre=input.nombre().trim();p.descripcion=input.descripcion();p.categoria=input.categoria();p.tono=input.tono();p.precioCompra=input.precioCompra();p.precioVenta=input.precioVenta();p.activo=input.activo();String name=images.save(image);if(name!=null)p.imagen=name;repo.saveAndFlush(p);remote.post(inventory+"/internal/inventario/"+p.id+"/inicializar",Map.of("stock",id==null?input.stock():0,"stockMinimo",input.stockMinimo()),Stock.class);return view(p);}
 @Transactional public void delete(long id){var p=entity(id);p.activo=false;repo.save(p);}
}