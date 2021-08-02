package reactor.study.reactor.reactivestream;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class PubSub {
	public static void main(String[] args) {
		Publisher<Integer> pub = new Publisher<Integer>() {
			@Override
			public void subscribe(Subscriber<? super Integer> sub) {
				// Subscriber -> 데이터 받는 거

				Iterable<Integer> iter = Stream.iterate(1, a -> a + 1).limit(10).collect(Collectors.toList());


				sub.onSubscribe(new Subscription() {
					@Override
					public void request(long n) {
						try {
							iter.forEach(sub::onNext);
							sub.onComplete();

						} catch (Throwable t) {
							sub.onError(t);
						}

					}

					@Override
					public void cancel() {

					}
				});
			}
		};

		Subscriber<Integer> sub = new Subscriber<Integer>() {
			// Publisher를 아래 4가지 메소드를 통해서 구독
			@Override
			public void onSubscribe(Subscription s) {
				log.debug("onSubscribe: ");
				s.request(Long.MAX_VALUE);
			}

			@Override
			public void onNext(Integer integer) { // 정상
				log.debug("onNext: {}", integer);
			}

			@Override
			public void onError(Throwable t) { // Exception Object
				log.debug("onNext: {}", t);
			}

			@Override
			public void onComplete() { // 전통 Observer와 다른..??
				log.debug("onComplete");
			}
		};

		pub.subscribe(sub);
	}
}
