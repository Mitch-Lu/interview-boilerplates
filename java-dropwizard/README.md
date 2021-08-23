# A simple dropwizard app.

**Requires Java 14 or greater**

pom.xml is configured to use java 14.

To run

```
mvn clean verify
./run.sh
```

The exercise code exposes a single endpoint at `localhost:8080/sentences/count` and a health check
at `localhost:8081/healthcheck`.

The endpoint accepts a `GET` request with a required QueryParam `word` and returns a json response.

The following example uses `src/main/resources/sentences-02.txt` as the resource.

`curl -i localhost:8080/sentences/count?word=eggplant`

    HTTP/1.1 200 OK
    Date: Mon, 23 Aug 2021 04:19:32 GMT
    Content-Type: application/json
    Vary: Accept-Encoding
    Content-Length: 349

 ```json
{
  "word": "eggplant",
  "sentences": [
    {
      "sentence": "Many people don't like eggplant.",
      "count": 1
    },
    {
      "sentence": "Did you say you like Eggplant or not eggplant?",
      "count": 2
    },
    {
      "sentence": "Do you like Eggplant or not eggplant?",
      "count": 2
    },
    {
      "sentence": "Eggplant or not eggplant?",
      "count": 2
    },
    {
      "sentence": "I like lots of Eggplant, eggplant, Eggplant!!!",
      "count": 3
    }
  ]
}
```

The Health check: `curl -i localhost:8081/healthcheck`

    HTTP/1.1 200 OK
    Date: Mon, 23 Aug 2021 04:22:03 GMT
    Content-Type: application/json
    Cache-Control: must-revalidate,no-cache,no-store
    Vary: Accept-Encoding
    Content-Length: 178

```json
{
  "AppHealthCheck": {
    "healthy": true,
    "duration": 0,
    "timestamp": "2021-08-22T21:22:03.298-07:00"
  },
  "deadlocks": {
    "healthy": true,
    "duration": 0,
    "timestamp": "2021-08-22T21:22:03.298-07:00"
  }
}
```
