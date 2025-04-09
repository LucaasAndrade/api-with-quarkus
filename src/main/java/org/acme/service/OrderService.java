package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.InternalServerErrorException;
import org.acme.dto.OrderDTO;
import org.acme.entity.Book;
import org.acme.entity.Loan;
import org.acme.entity.Reader;
import org.acme.enums.StatusEnum;

@ApplicationScoped
public class OrderService {

    @Transactional
    public Loan createOrder(OrderDTO dto) {
            Book book = Book.findById(dto.bookId);
            Reader reader = Reader.findById(dto.readerId);
            if(book == null || reader == null) {
                throw new IllegalArgumentException("Book or Reader not found");
            }
            Loan order = new Loan();
            order.book = book;
            order.reader = reader;

            order.persist();
            return order;
    }

    @Transactional
    public Loan updateStaus(Long id, String status) {
        Loan order = Loan.findById(id);
        if(order == null) {
            throw new IllegalArgumentException("Order not found");
        }
        switch (status.toUpperCase()) {
            case "ACTIVE":
                order.statusEnum = StatusEnum.ACTIVE;
                break;
            case "CANCELED":
                order.statusEnum = StatusEnum.CANCELED;
                break;
            case "LATE":
                order.statusEnum = StatusEnum.LATE;
                break;
            case "DELIVERED":
                order.statusEnum = StatusEnum.DELIVERED;
                break;
            default:
                throw new IllegalArgumentException("Invalid status");
        }
        try {
            order.persist();
        } catch (Exception e) {
            throw new InternalServerErrorException("Transaction failed");
        }
        return order;
    }
}
