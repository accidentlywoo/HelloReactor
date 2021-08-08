package reactor.study.reactor.reactivestream;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SchedulerEx {
	public static void main(String[] args) {
		Publisher<Integer> pub = sub -> {
			sub.onSubscribe(new Subscription() {
				@Override
				public void request(long n) {
					log.info("request()");
					sub.onNext(1);
					sub.onNext(2);
					sub.onNext(3);
					sub.onNext(4);
					sub.onNext(5);
					sub.onComplete();
				}

				@Override
				public void cancel() {
				}
			});
		};

		Publisher<Integer> subOnPub = sub -> {
			ExecutorService es = Executors.newSingleThreadExecutor();
			es.execute(() ->pub.subscribe(sub));
		};

		// sub

		subOnPub.subscribe(new Subscriber<Integer>() {
			@Override
			public void onSubscribe(Subscription s) {
				log.info("onSubscribe");
				s.request(Long.MAX_VALUE); // 이 값까지 날리겠따!
			}

			@Override
			public void onNext(Integer integer) {
				log.info("onNext: {}", integer);
			}

			@Override
			public void onError(Throwable t) {
				log.info("onError: {}", t);
			}

			@Override
			public void onComplete() {
				log.info("onComplete");
			}
		});

		System.out.println("Exit");
	}
}
