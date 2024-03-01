package src.main.java.com.project.chatkeep.memo.controller;

import hello.src.main.java.com.project.chatkeep.memo.domain.Memo;
import hello.src.main.java.com.project.chatkeep.memo.service.MemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MemoController {

    private final MemoService memoService;

    @Autowired
    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    @GetMapping("/memos/new")
    public String creatForm() {
        return "memos/createMemoForm";
    }

    @PostMapping("memos/new")
    public String create(MemoForm form) {
        Memo memo = new Memo();
        memo.setTitle(form.getTitle());
        memo.setContent(form.getContent());
        memoService.save(memo);
        return "redirect:/";
    }

    @GetMapping("/api/memos/{memoId}")
    public ResponseEntity<Memo> getMemoById(@PathVariable Long memoId) {
        return memoService.findById(memoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/memos")
    public String list(Model model) {
        List<Memo> memos = memoService.findMemos();
        model.addAttribute("memos", memos);
        return "memos/memoList";
    }

    // gpt
    @PostMapping("/memos/{memoId}/bookmark")
    public String bookmark(@PathVariable Long memoId) {
        memoService.bookmarkMemo(memoId);
        return "redirect:/memos";
    }

    @PostMapping("/memos/{memoId}/unbookmark")
    public String unbookmark(@PathVariable Long memoId) {
        memoService.unbookmarkMemo(memoId);
        return "redirect:/memos";
    }
}
