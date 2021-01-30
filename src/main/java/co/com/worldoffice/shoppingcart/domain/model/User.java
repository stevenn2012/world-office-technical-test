package co.com.worldoffice.shoppingcart.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Table
@Entity
@Getter
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Integer id;

    @NonNull
    @Column(nullable = false, unique = true)
    private String ip;

    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private List<Product> products;
}
