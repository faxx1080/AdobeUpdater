function FindProxyForURL(url, host) {
  if (shExpMatch(host, "ccmdls.adobe.com")) return "PROXY 127.0.0.1:8000";
  return "DIRECT";
}
