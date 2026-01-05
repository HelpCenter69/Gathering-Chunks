#!/bin/bash

# Script to fix Minecraft 1.21.1 recipe format
# In the "key" section: "id" should be "item"
# In the "result" section: keep "id" as is

RECIPE_DIR="Fabric/src/test/resources/data/chunkbychunk/recipes"

echo "Fixing recipe files in: $RECIPE_DIR"
echo "========================================"

# Backup first
echo "Creating backup..."
cp -r "$RECIPE_DIR" "${RECIPE_DIR}_backup_$(date +%Y%m%d_%H%M%S)"

# Find all .json files in the recipes directory
find "$RECIPE_DIR" -name "*.json" | while read -r file; do
    echo "Processing: $(basename $file)"

    # Use perl for more precise regex replacement
    # This replaces "id" with "item" ONLY when it appears in a key context
    # (after "key" and before "result")
    perl -i -pe '
        BEGIN { $in_key = 0; $in_result = 0; }

        # Detect when we enter the key section
        if (/"key"\s*:/) {
            $in_key = 1;
        }

        # Detect when we enter result section (exit key section)
        if (/"result"\s*:/) {
            $in_key = 0;
            $in_result = 1;
        }

        # Only replace "id" : with "item" : when inside key section
        if ($in_key && /"id"\s*:/) {
            s/"id"\s*:/"item":/g;
        }
    ' "$file"

done

echo "========================================"
echo "Done! All recipe files have been updated."
echo ""
echo "Changes made:"
echo "  - In 'key' section: replaced '\"id\"' with '\"item\"'"
echo "  - In 'result' section: kept '\"id\"' unchanged"
echo ""
echo "Backup created at: ${RECIPE_DIR}_backup_*"
echo ""
echo "Now rebuild your mod with: ./gradlew build"