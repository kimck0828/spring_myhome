package com.example.myhome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50, message = "タイトルは２～５０文字で入力してください")
    @NotBlank(message = "タイトルを入力してください")
    private String title;

    @NotNull
    private String content;
    
    @ManyToOne
    @JoinColumn(
            // BOARDテーブルとUSERテーブルをJOINするBOARDテーブルカラム
            name="user_id",
            // BOARDテーブルとUSERテーブルをJOINするUSERテーブルカラム
            // ※JOINカラムがPKの場合は省略出来る。（「referencedColumnName = "id"」がなくてもOK）
            referencedColumnName = "id"
    )
    @JsonIgnore
    private User user;
}
