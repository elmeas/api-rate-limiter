package com.elmeas.interview.service;

import java.time.Clock;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class InMemoryRateLimiter {
  static final int REQUEST_LIMIT = 100;
  static final Duration WINDOW_DURATION = Duration.ofMinutes(1);

  private final ConcurrentHashMap<String, RequestWindow> requestWindows =
      new ConcurrentHashMap<>();
  private final Clock clock;

  public InMemoryRateLimiter() {
    this(Clock.systemUTC());
  }

  InMemoryRateLimiter(Clock clock) {
    this.clock = clock;
  }

  public boolean allowRequest(String userId) {
    long now = clock.millis();
    RequestWindow window = requestWindows.computeIfAbsent(
        userId, ignored -> new RequestWindow(now));

    if (now - window.startedAtMillis >= WINDOW_DURATION.toMillis()) {
      RequestWindow replacement = new RequestWindow(now);
      requestWindows.replace(userId, window, replacement);
      window = requestWindows.get(userId);
    }

    if (window.requestCount.get() >= REQUEST_LIMIT) {
      return false;
    }

    window.requestCount.incrementAndGet();
    return true;
  }

  public void clear() {
    requestWindows.clear();
  }

  private static final class RequestWindow {
    private final long startedAtMillis;
    private final AtomicInteger requestCount = new AtomicInteger();

    private RequestWindow(long startedAtMillis) {
      this.startedAtMillis = startedAtMillis;
    }
  }
}
