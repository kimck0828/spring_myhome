package com.example.myhome.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private Boolean enabled;
    
    @ManyToMany
    @JoinTable(
            // 結合表（対象テーブルとの結合するためのテーブル）
            name="user_role",
            // 結合表でBOARDテーブルの値になるカラム名
            joinColumns = @JoinColumn(name = "user_id"),
            // 結合表でROLEテーブルの値になるカラム名
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();
    
    @OneToMany(
            // BoardクラスでマッピングされるUserクラス内の変数
            mappedBy = "user",
            // user情報変更時にBOARDテーブルの処理を実行する
            // ALLなので、ユーザが削除されると、ユーザのBOARDがすべて削除されたり
            // boardsを変更するとBOARDテーブルの更新処理も実行される
            cascade = CascadeType.ALL
    )
    private List<Board> boards = new ArrayList<>();

}
