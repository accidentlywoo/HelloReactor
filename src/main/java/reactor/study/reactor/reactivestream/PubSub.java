package reactor.study.reactor.reactivestream;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Publisher -> [Data1] -> Operator1 -> [Data2] -> Operator2 -> Subscriber
 * <p>
 * 1. map (data1 -> function -> data2)
 */
@Slf4j
public class PubSub {
	public static void main(String[] args) {
		Publisher<Integer> pub = iterPub(Stream.iterate(1, a -> a + 1).limit(10).collect(Collectors.toList()));
		Publisher<Integer> mapPub = mapPub(pub, s -> s * 10);
		mapPub.subscribe(logSub()); //
	}

	private static <T> Publisher<T> mapPub(Publisher<T> pub, Function<T, T> func) {
		return new Publisher<T>() {
			@Override
			public void subscribe(Subscriber<? super T> sub) {
				pub.subscribe(new DelegateSub<T>(sub) {
					@Override
					public void onNext(T integer) {
						sub.onNext(func.apply(integer));
					}
				});
			}
		};
	}

	private static <T> Subscriber<T> logSub() {
		Subscriber<T> sub = new Subscriber<T>() {
			// Publisher를 아래 4가지 메소드를 통해서 구독
			@Override
			public void onSubscribe(Subscription s) {
				log.debug("onSubscribe: ");
				s.request(Long.MAX_VALUE);
			}

			@Override
			public void onNext(T t) { // 정상
				log.debug("onNext: {}", t);
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
		return sub;
	}

	private static Publisher<Integer> iterPub(Iterable<Integer> iter) {
		return new Publisher<Integer>() {

			@Override
			public void subscribe(Subscriber<? super Integer> sub) {
				try {
					iter.forEach(sub::onNext);
					sub.onComplete();

				} catch (Throwable t) {
					sub.onError(t);
				}
			}
		};
	}
}
