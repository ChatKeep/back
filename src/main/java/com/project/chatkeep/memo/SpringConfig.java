package src.main.java.com.project.chatkeep.memo;
import hello.hellospring.repository.*;
import hello.src.main.java.com.project.chatkeep.memo.repository.MemberRepository;
import hello.src.main.java.com.project.chatkeep.memo.repository.MemoRepository;
import hello.src.main.java.com.project.chatkeep.memo.service.MemberService;
import hello.src.main.java.com.project.chatkeep.memo.service.MemoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class SpringConfig {
    private final MemberRepository memberRepository;
    private final MemoRepository memoRepository;

    // 생성자에서 두 리포지토리를 주입받습니다.
    public SpringConfig(MemberRepository memberRepository, MemoRepository memoRepository) {
        this.memberRepository = memberRepository;
        this.memoRepository = memoRepository;
    }

    // MemberService 빈을 생성하고 등록합니다.
    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository);
    }

    // MemoService 빈을 생성하고 등록합니다.
    @Bean
    public MemoService memoService() {
        return new MemoService(memoRepository);
    }
}