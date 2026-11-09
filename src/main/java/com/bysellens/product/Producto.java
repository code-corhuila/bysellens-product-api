package com.bysellens.product;
import jakarta.persistence.*;import java.math.BigDecimal;
@Entity @Table(name="productos") public class Producto {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(unique=true,nullable=false) public String codigo;
 public String nombre,descripcion,categoria,tono,imagen="";
 @Column(precision=14,scale=2) public BigDecimal precioCompra,precioVenta;
 public boolean activo=true;
}