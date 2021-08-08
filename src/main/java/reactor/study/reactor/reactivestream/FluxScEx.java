package reactor.study.reactor.reactivestream;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FluxScEx {
	public static void main(String[] args) throws InterruptedException {
		Flux.interval(Duration.ofMillis(500)) // Demon Thread를 만든다.
				.take(10) // 10개 데이터를 받으면 종료
				.subscribe(s -> log.info("onNext: {}", s));

		System.out.println("Exit");
		TimeUnit.SECONDS.sleep(5);

//      User / Deamon Thread : JVM이 데몬 스레드만 남아있으면, Flux를 종료해버림. User 스레드가 하나라도 남아있으면 종료되지 않음
//		Executors.newSingleThreadExecutor().execute(() -> {
//			try {
//				TimeUnit.SECONDS.sleep(5);
//			} catch (InterruptedException e) { }
//			System.out.println("Hello");
//		});// User Thread는 Main Thread가 종료가 되도 종료되지 않는다. 강제 종료나 작업을 마치거나 Interrupt에 걸려야 종료된다/
//
//		System.out.println("Exit");
	}
}
