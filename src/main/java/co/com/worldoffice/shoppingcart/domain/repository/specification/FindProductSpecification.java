package co.com.worldoffice.shoppingcart.domain.repository.specification;

import co.com.worldoffice.shoppingcart.domain.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class FindProductSpecification implements Specification<Product> {

    private final String name;
    private final String brand;
    private final Double lowestPrice;
    private final Double highestPrice;

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();
        if (name != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%".concat(name.toUpperCase()).concat("%")));
        }

        if (brand != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("brand")), "%".concat(brand.toUpperCase()).concat("%")));
        }
        Expression<Double> price = root.get("price");
        Expression<Double> discountPercent = root.get("discountPercent");

        Expression<Number> division = criteriaBuilder.quot(discountPercent, 100);
        Expression<Number> subtraction = criteriaBuilder.diff(1, division);
        Expression<BigDecimal> finalPrice = criteriaBuilder.prod(price, subtraction).as(BigDecimal.class);

        if (lowestPrice != null && highestPrice != null) {
            predicates.add(getPriceBetweenPredicate(price, finalPrice, criteriaBuilder));
        } else {
            if (lowestPrice != null) {
                predicates.add(getOnlyLowestPricePredicate(price, finalPrice, criteriaBuilder));
            }

            if (highestPrice != null) {
                predicates.add(getOnlyHighestPricePredicate(price, finalPrice, criteriaBuilder));
            }
        }

        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }

    private Predicate getOnlyHighestPricePredicate(Expression<Double> price, Expression<BigDecimal> finalPrice, CriteriaBuilder criteriaBuilder) {
        Predicate p1 = criteriaBuilder.lessThanOrEqualTo(price, highestPrice);
        Predicate p2 = criteriaBuilder.lessThanOrEqualTo(finalPrice, BigDecimal.valueOf(highestPrice));
        return criteriaBuilder.or(p1, p2);
    }

    private Predicate getOnlyLowestPricePredicate(Expression<Double> price, Expression<BigDecimal> finalPrice, CriteriaBuilder criteriaBuilder) {
        Predicate p1 = criteriaBuilder.greaterThanOrEqualTo(price, lowestPrice);
        Predicate p2 = criteriaBuilder.greaterThanOrEqualTo(finalPrice, BigDecimal.valueOf(lowestPrice));
        return criteriaBuilder.or(p1, p2);
    }

    private Predicate getPriceBetweenPredicate(Expression<Double> price, Expression<BigDecimal> finalPrice,
                                               CriteriaBuilder criteriaBuilder) {
        Predicate p1 = criteriaBuilder.between(price, lowestPrice, highestPrice);
        Predicate p2 = criteriaBuilder.between(finalPrice, BigDecimal.valueOf(lowestPrice), BigDecimal.valueOf(highestPrice));
        return criteriaBuilder.or(p1, p2);
    }
}
