import json
import time
import websocket

WS_URL = "ws://localhost:26000/ws"
TOPIC = "/topic/albuns/novos"

def stomp_frame(command: str, headers=None, body: str = "") -> str:
    headers = headers or {}
    lines = [command]
    for k, v in headers.items():
        lines.append(f"{k}:{v}")
    return "\n".join(lines) + "\n\n" + (body or "") + "\x00"

def parse_frames(raw: str):
    frames = []
    parts = raw.split("\x00")
    for part in parts:
        if not part.strip():
            continue
        part = part.lstrip("\n")
        sep = part.find("\n\n")
        if sep == -1:
            continue
        head = part[:sep]
        body = part[sep + 2:]
        head_lines = head.split("\n")
        cmd = head_lines[0].strip()
        headers = {}
        for line in head_lines[1:]:
            if ":" in line:
                k, v = line.split(":", 1)
                headers[k.strip()] = v.strip()
        frames.append((cmd, headers, body))
    return frames

def main():
    ws = websocket.create_connection(
        WS_URL,
        subprotocols=["v12.stomp", "v11.stomp", "v10.stomp"],
        timeout=3600
    )

    # CONNECT
    ws.send(stomp_frame("CONNECT", {
        "accept-version": "1.2",
        "host": "localhost",
        # se você quiser heartbeat:
        # "heart-beat": "10000,10000",
        # se quiser mandar token no CONNECT (às vezes o backend lê):
        # "Authorization": "Bearer <TOKEN>",
    }))

    # Aguarda CONNECTED
    while True:
        data = ws.recv()
        for cmd, headers, body in parse_frames(data):
            if cmd == "CONNECTED":
                print("✅ Conectado:", headers)
                break
            if cmd == "ERROR":
                raise RuntimeError(f"❌ ERROR: {body or headers}")
        else:
            continue
        break

    # SUBSCRIBE
    ws.send(stomp_frame("SUBSCRIBE", {
        "id": "sub-1",
        "destination": TOPIC,
        "ack": "auto",
    }))
    print(f"👂 Ouvindo {TOPIC} ... (Ctrl+C para sair)")

    try:
        while True:
            data = ws.recv()
            for cmd, headers, body in parse_frames(data):
                if cmd == "MESSAGE":
                    try:
                        payload = json.loads(body)
                    except Exception:
                        payload = body
                    print("📩 Evento:", payload)
                elif cmd == "ERROR":
                    print("❌ ERROR:", body or headers)
            time.sleep(0.01)
    except KeyboardInterrupt:
        print("\n🛑 Encerrando...")
    finally:
        try:
            ws.send(stomp_frame("DISCONNECT", {}))
        except Exception:
            pass
        ws.close()

if __name__ == "__main__":
    main()
