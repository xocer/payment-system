package com.grishin.individuals.api.util;

import com.grishin.dto.TokenResponse;

public class DataUtils {
    public static TokenResponse getTokenResponse() {
        return new TokenResponse().accessToken("""
                eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJtNjlSeGt4ZlZUdElFM1JjbWxkUFFqVVhfZV9fRGpWR0FM
                MlZwcy1sSks0In0.eyJleHAiOjE3NTEyOTQzNDksImlhdCI6MTc1MTI5NDA0OSwianRpIjoib25ydHJvOmJhMTkyODViLWJjO
                TgtNDJhMS04NDlhLTgwNDFmYmU3ZGViMiIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODk4MC9yZWFsbXMvcGF5bWVudC1zeX
                N0ZW0iLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiNWM4NmYyYWEtMDZhNy00NWFkLWFhNGEtMjUxN2FhZmYzMzlmIiwidHlwIjoi
                QmVhcmVyIiwiYXpwIjoiaW5kaXZpZHVhbHMtYXBpIiwic2lkIjoiMGM0YTRiOGMtNTJhNS00YTJhLTk4MGYtOWM1YTA3YTRkN
                TAxIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0Ojg5ODAiXSwicmVhbG1fYWNjZXNzIj
                p7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtcGF5bWVudC1zeXN0ZW0iLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF
                0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY
                2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgZW1haWwgcHJvZmlsZSIsImVtYWlsX3Zlcml
                maWVkIjp0cnVlLCJuYW1lIjoiTWlzaGEgUGV0cm92IiwicHJlZmVycmVkX3VzZXJuYW1lIjoicGV0cm92QG1haWwuY29tIiwiZ
                2l2ZW5fbmFtZSI6Ik1pc2hhIiwiZmFtaWx5X25hbWUiOiJQZXRyb3YiLCJlbWFpbCI6InBldHJvdkBtYWlsLmNvbSJ9.F_qFsG9
                YZoe_xrEay8A8w5eVso4Q47C0rPcej7P4LQtr6g3CL1hS1jr-6IQWdg_B9uek7K4ASJVE3CXapkKq5TuTmxYZyT1ldPa75yyVmJ
                rwKgQKL5AqQ7CySf3ByXr8D0nzLGJGEeu8R90adXLxxp6giU7Zm88z2949TrZCSPH4gBEw7vGVBhuixDU_DpEBVLSMckY9Ue9UkD
                7sQb23TbtthNXvxfYYyw_PorjSjujqkfHl3FQFJc5AHBBXnfR9MFbie8qohFzEHuexmUVOaa4BSH9870pkmGckdixOCZom7FWMnr
                SADrjNvCOk03V-O8OuLo9AAME-GHEi2xE9Og
                """)
                .refreshToken("""
                        eyJhbGciOiJIUzUxMiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI4MWUzMDM2ZS05ZTdhLTRhODYtYTQ0YS01ZmZjMD
                        g2N2VmYjEifQ.eyJleHAiOjE3NTEyOTU4NDksImlhdCI6MTc1MTI5NDA0OSwianRpIjoiNTJkNWZiMGYtYTI5Zi00O
                        TRiLTk4ZTktYzExNTA1OTFiNTM5IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4OTgwL3JlYWxtcy9wYXltZW50LXN
                        5c3RlbSIsImF1ZCI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODk4MC9yZWFsbXMvcGF5bWVudC1zeXN0ZW0iLCJzdWIiOiI1
                        Yzg2ZjJhYS0wNmE3LTQ1YWQtYWE0YS0yNTE3YWFmZjMzOWYiLCJ0eXAiOiJSZWZyZXNoIiwiYXpwIjoiaW5kaXZpZHV
                        hbHMtYXBpIiwic2lkIjoiMGM0YTRiOGMtNTJhNS00YTJhLTk4MGYtOWM1YTA3YTRkNTAxIiwic2NvcGUiOiJvcGVuaW
                        Qgd2ViLW9yaWdpbnMgc2VydmljZV9hY2NvdW50IGFjciByb2xlcyBlbWFpbCBiYXNpYyBwcm9maWxlIn0.IYssKHte2
                        lAcnpvhpvRl3AX8urig8PTz5HeAceFMBKrPXlQswHSHNRp-lk8r54Zt-8UeWO20rH0mM8tvDcaFgg
                        """)
                .expiresIn(300)
                .tokenType("Bearer");
    }
}
