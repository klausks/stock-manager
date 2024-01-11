package cids.demo.productstockmanager.supplier.application.port.out;

import cids.demo.productstockmanager.supplier.domain.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
