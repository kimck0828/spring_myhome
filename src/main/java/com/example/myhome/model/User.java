package com.example.myhome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter@Setter
@Table(name = "user")
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
            // 結合表でUSERテーブルの値になるカラム名
            joinColumns = @JoinColumn(name = "user_id"),
            // 結合表でROLEテーブルの値になるカラム名
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    // JSON出力対象外にする
    @JsonIgnore
    private List<Role> roles = new ArrayList<>();
    
    @OneToMany(
            // BoardクラスでマッピングされるUserクラス内の変数
            mappedBy = "user",
            // user情報変更時にBOARDテーブルの処理を実行する
            // ALLなので、ユーザが削除されると、ユーザのBOARDがすべて削除されたり
            // boardsを変更するとBOARDテーブルの更新処理も実行される
//            cascade = CascadeType.ALL
            
            // LAZY : 対象変数に該当するテーブルの参照が発生する時にデータ取得が発生する
            // EAGER：対象変数に該当するテーブルを無条件に取得する
            fetch = FetchType.LAZY
    )
    // JSON出力対象外にする
    @JsonIgnore
    private List<Board> boards = new ArrayList<>();

}
