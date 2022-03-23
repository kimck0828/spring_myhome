package com.example.myhome.validator;

import com.example.myhome.model.Board;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.thymeleaf.util.StringUtils;

@Component
public class BoardValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Board.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Board board = (Board) target;
//        if (StringUtils.isEmpty(board.getTitle())) {
//            errors.rejectValue("title", "key", "タイトルを入力してください");
//        }
        if (StringUtils.isEmpty(board.getContent())) {
            errors.rejectValue("content", "key", "コンテンツを入力してください");
        }
    }
}
