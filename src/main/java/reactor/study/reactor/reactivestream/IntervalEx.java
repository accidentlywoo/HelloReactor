package reactor.study.reactor.reactivestream;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class IntervalEx {
	public static void main(String[] args) {
		Publisher<Integer> pub = sub -> {
			sub.onSubscribe(new Subscription() {
				int no = 0;
				boolean cancelled = false;

				@Override
				public void request(long n) {
					ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
					exec.scheduleAtFixedRate(() -> {
						if(cancelled) {
							exec.shutdown();
							return;
						}
						sub.onNext(no++);
					}, 0, 300, TimeUnit.MILLISECONDS);
				}

				@Override
				public void cancel() {
					cancelled = true;
				}
			});
		};

		Publisher<Integer> takePub = sub -> {
			pub.subscribe(new Subscriber<Integer>() {
				int count = 0;
				Subscription subsc;

				@Override
				public void onSubscribe(Subscription s) {
					subsc = s;
					sub.onSubscribe(s);
				}

				@Override
				public void onNext(Integer integer) {
					sub.onNext(integer);
					if(++count > 9) {
						subsc.cancel();
					}
				}

				@Override
				public void onError(Throwable t) {
					sub.onError(t);
				}

				@Override
				public void onComplete() {
					sub.onComplete();
				}
			});
		};

		takePub.subscribe(new Subscriber<Integer>() {
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
	}
}
