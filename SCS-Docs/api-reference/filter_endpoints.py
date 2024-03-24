import json

# open json file and convert to a dictionary
openapi = json.load(open("openapi.json", "r"))

# go through all the paths, remove the ones in which the name's end with "/"
for path in list(openapi["paths"]):
    if path.endswith("/"):
        del openapi["paths"][path]

# save the new openapi.json
with open("openapi.json", "w") as f:
    json.dump(openapi, f, indent=4)