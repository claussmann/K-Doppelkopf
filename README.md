# K-Doppelkopf
The german card game Doppelkopf

## Run (Docker/Podman)

Build and run the container as follows.

```bash
podman build -t doppelkopfserver
podman run -p 8080:8080 doppelkopfserver

```

Then visit [localhost:8080](http://localhost:8080/).
Note that this game needs four players.
So you would run the container on a server and call the url from 4 different devices to let four players join.