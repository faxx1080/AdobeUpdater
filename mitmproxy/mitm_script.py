"""Send a reply from the proxy without sending any data to the remote server."""
from mitmproxy import http
import urllib.parse
import os

files_cached = []
try:
    files_cached = os.listdir('/mnt/c/Apache24/htdocs')
except:
    files_cached = os.listdir('C:/Apache24/htdocs')

def request(flow: http.HTTPFlow) -> None:
    url = flow.request.pretty_url
    if 'ccmdls.adobe.com/AdobeProducts' in url:
        filename = url[url.rindex('/')+1:]
        filename = urllib.parse.unquote(filename)
        if filename in files_cached:
            flow.response = http.Response.make(
                301,  # (optional) status code
                b"",  # (optional) content
                {"Location": "http://127.0.0.1:80/"+filename}  # (optional) headers
            )
        