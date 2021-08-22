# Adobe App Local Cache

Until they figure out what "delta updates" are for people who don't have infinite bandwidth.

1. Hardlinker
    1. Store start date
    2. Scan %temp% for new folders
    3. Scan each of these folders for a new aamdownload file
        1. Eample: AdobeIllustrator25-Core.zip.aamdownload
    1. If found:
        1. Hardlink that download to a safe folder
        2. Rename the hardlink to end in zip
        3. Save filename to dictionary so we don&#39;t repeat
    1. Wait one second; repeat. 


1. Cache
    1. Move files from (1) to a shared cache accessible by the HTTP server and mitmproxy
    2. Mitmproxy and the HTTP server should be on the same endpoint.
    3. Due to a bug(?) with Adobe, once a proxy is chosen for an endpoint that proxy will be used even if a redirect would send it to another proxy.
    4. Must add a proxy.pac file to only redirect ccmdls.adobe.com to local cache

1. Proxy
    1. Given: Folder of cached ZIP downloads
    2. Need
        1. Test if file:/// is accepted as a 301 redirect; NOPE
        2. Mitmproxy
            1. Make sure stream\_large\_bodies is set to &#39;1m&#39; in the config.yaml file
            2. Cannot use &quot;map\_local&quot; this crashes due to size of file. MUST be a manual redirect
        1. Python script to redirect to a local HTTP server

1. OS Config
    1. Apache 2.4
        1. Download from [https://www.apachelounge.com/download/](https://www.apachelounge.com/download/)
        2. Extract to C:\
        3. In C:\apache24\htdocs, this is the web folder
        4. Run C:\apache24\bin\httpd
    1. Proxy PAC file
        1. Contents in repo
        2. Move to C:\apache24\htdocs
    1. Windows search for &quot;Proxy settings&quot;
        1. Set setup script to &quot;[http://127.0.0.1/proxy.pac](http://127.0.0.1/proxy.pac)&quot;
    1. Mitmproxy
        1. Download from [https://mitmproxy.org/](https://mitmproxy.org/)
        2. Extract anywhere
        3. Run initially
            1. Download MITMPROXY for windows
            2. Run it once
            3. Manually load the proxy
            4. Go to mitm.it
            5. Download the cert
            6. Install into trusted certs (requires ADMIN)
        1. Copy config.yaml \&gt; ~/.mitmproxy
        2. Run mitmproxy
  1. Done

