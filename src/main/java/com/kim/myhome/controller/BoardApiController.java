package com.kim.myhome.controller;

import com.kim.myhome.model.Board;
import com.kim.myhome.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BoardApiController {
    
        @Autowired
        private  BoardRepository repository;

        @GetMapping("/boards")
        List<Board> all(@RequestParam(required = false)String title,
                        @RequestParam(required = false, defaultValue = "")String content) {
            if(StringUtils.isEmpty(title) && StringUtils.isEmpty(content)){
                return repository.findAll();
            }else{
                return  repository.findByTitleOrContent(title,content);
            }

        }

        @PostMapping("/boards")
        Board newBoard(@RequestBody Board newBoard) {
            return repository.save(newBoard);
        }

        @GetMapping("/boards/{id}")
        Board one(@PathVariable Long id) {

            return repository.findById(id).orElse(null);

        }

        @PutMapping("/boards/{id}")
        Board replaceBoard(@RequestBody Board newBoard, @PathVariable Long id) {

            return repository.findById(id)
                    .map(board -> {
                        board.setTitle(newBoard.getTitle());
                        board.setContent(newBoard.getContent());
                        return repository.save(board);
                    })
                    .orElseGet(() -> {
                        newBoard.setId(id);
                        return repository.save(newBoard);
                    });
        }

        @DeleteMapping("/boards/{id}")
        void deleteBoard(@PathVariable Long id) {
            repository.deleteById(id);
        }
    
    
}
