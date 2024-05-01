package com.jh.dividendpj.company.domain;

import com.jh.dividendpj.dividend.domain.Dividend;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@SQLDelete(sql = "UPDARE company SET delDate = now() WHERE ticker=?")
@SQLRestriction("delDate = null")
@ToString
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Dividend> devidendList;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String ticker;

    @Column
    private LocalDateTime delDate;
}
