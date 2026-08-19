# Interview Task: Review and Fix an API Rate Limiter

## Instructions:

1. **Clone the Repository**  
   Please clone the following repository to your local machine:  
   [https://github.com/elmeas/api-rate-limiter](https://github.com/elmeas/api-rate-limiter)
   
2. **Setup**  
   Follow the instructions in the `README.md` to set up the API environment. Make sure the API runs correctly before starting the implementation.

## Core Task:

### Review the Existing Rate Limiter
- The repository contains an in-memory rate limiter for `GET /hello`.
- Review the implementation and fix any correctness or thread-safety problems you find.
- The endpoint must restrict each user to **100 requests per minute**. The user is identified by the `X-User-Id` request header.
- If the rate limit is exceeded, return a response with **HTTP 429 (Too Many Requests)** status code.
- Ensure your solution is **thread-safe** and handles concurrent requests properly.
- Continue using an **in-memory Java data structure** to track request counts.

### Expectations:
- Write **clean, maintainable code**.
- Add tests that demonstrate the problem and validate your solution, including concurrent requests.
- You may use Copilot, ChatGPT, Codex, or another AI tool. Be prepared to explain and modify every part of your solution during the follow-up discussion.

## Bonus Task:

### Implement Distributed Rate Limiting
- Instead of using an in-memory store, implement **distributed rate limiting** using a system like **Redis**. You can use a docker-compose to lunch a Redis instance
- Ensure the rate limit applies across multiple servers, where the total requests made by a user on different instances are aggregated.
  
  For example, if a user makes 50 requests on Instance A and 50 on Instance B, they should still be limited to 100 requests per minute.

### Additional Requirements:
- Design for **high availability**: Ensure your solution can handle cases where Redis might experience downtime, implementing retries, circuit breakers, or fallbacks if needed.
- Consider implementing an efficient rate-limiting algorithm like **leaky bucket** or **token bucket** to manage traffic efficiently.
