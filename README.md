# clear-contentful-space-java

This program lets you delete  entries of a contentful space. Zou can either delete all entries of a certain content type or all entries of the space.

Program parameters:
-space: The contentful space id
-env: The contentful environment name
-token: The contentful management API access token of the given space
-type: The contentful content type ID for which all entries will be deleted
-all: A flag which denotes that all entries independent of type should be deleted

Please note, either "-type" or "-all" must be set.

Example: java -jar com.virtualidentity.clearcontentfulspace.ClearContentfulSpaceApplication -token CFPAT-zdtYIhtI-O2W9-ift6KJ-cP7DP6gVmcCyaEySAcvntQ -space f7lhvowaprbt -env dev -type news