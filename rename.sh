#!/bin/bash

HEADER='/*
 * Original work Copyright (c) immortius
 * Modified work Copyright (c) 2025 Ryvione
 *
 * This file is part of Chunk By Chunk (Ryvione'\''s Fork).
 * Original: https://github.com/immortius/chunkbychunk
 *
 * Licensed under the MIT License. See LICENSE file in the project root for details.
 */
'

echo "=================================="
echo "Chunk By Chunk - Fork Migration"
echo "=================================="
echo ""

echo "Step 1: Processing Java files..."
echo "  - Removing comments"
echo "  - Adding copyright headers"
echo "  - Updating package names"
echo ""

find . -type f -name "*.java" ! -path "*/build/*" ! -path "*/.gradle/*" | while read -r file; do
    echo "  Processing: $file"

    temp_file="${file}.tmp"

    sed '
        s|//.*$||g;
        /\/\*/,/\*\//d;
        /^\s*\*.*$/d;
    ' "$file" | sed '/^[[:space:]]*$/d' > "$temp_file"

    if grep -q "^package" "$temp_file"; then
        awk -v header="$HEADER" '
            /^package/ {
                print header
                gsub(/xyz\.immortius\.chunkbychunk/, "com.ryvione.chunkbychunk", $0)
                print
                next
            }
            {
                gsub(/xyz\.immortius\.chunkbychunk/, "com.ryvione.chunkbychunk", $0)
                print
            }
        ' "$temp_file" > "$file"
    else
        echo "$HEADER" > "$file"
        cat "$temp_file" >> "$file"
    fi

    rm -f "$temp_file"
done

echo ""
echo "Step 2: Updating resource files (JSON, TOML, etc.)..."
echo ""

find . -type f \( -name "*.json" -o -name "*.toml" -o -name "*.mcmeta" -o -name "*.gradle" \) ! -path "*/build/*" ! -path "*/.gradle/*" | while read -r file; do
    echo "  Processing: $file"
    sed -i.bak 's/xyz\.immortius\.chunkbychunk/com.ryvione.chunkbychunk/g' "$file"
    sed -i.bak "s/xyz\\.immortius/com.ryvione/g" "$file"
    rm -f "${file}.bak"
done

echo ""
echo "Step 3: Renaming directory structure..."
echo "  From: xyz/immortius/chunkbychunk"
echo "  To:   com/ryvione/chunkbychunk"
echo ""

find . -type d -path "*/xyz/immortius/chunkbychunk" ! -path "*/build/*" ! -path "*/.gradle/*" | sort -r | while read -r old_path; do
    parent_dir=$(dirname "$old_path" | sed 's|/xyz/immortius/chunkbychunk||' | sed 's|/xyz/immortius||' | sed 's|/xyz||')
    new_path="${parent_dir}/com/ryvione/chunkbychunk"

    echo "  Moving: $old_path"
    echo "      To: $new_path"

    mkdir -p "$new_path"

    cp -r "$old_path"/* "$new_path/" 2>/dev/null || true

    rm -rf "$old_path"
done

echo ""
echo "Step 4: Cleaning up old xyz/immortius directories..."
echo ""

find . -type d \( -name "immortius" -o -name "xyz" \) ! -path "*/build/*" ! -path "*/.gradle/*" | sort -r | while read -r old_dir; do
    if [ -d "$old_dir" ] && [ -z "$(ls -A "$old_dir" 2>/dev/null)" ]; then
        echo "  Removing empty: $old_dir"
        rm -rf "$old_dir"
    fi
done

echo ""
echo "Step 5: Updating META-INF service files..."
echo ""

find . -type f -path "*/META-INF/services/*" ! -path "*/build/*" | while read -r service_file; do
    if [[ "$service_file" == *"xyz.immortius"* ]]; then
        new_service_file="${service_file//xyz.immortius.chunkbychunk/com.ryvione.chunkbychunk}"
        new_service_dir=$(dirname "$new_service_file")
        echo "  Renaming service: $(basename "$service_file")"
        mkdir -p "$new_service_dir"
        mv "$service_file" "$new_service_file" 2>/dev/null || true
    fi

    if [ -f "$service_file" ]; then
        echo "  Updating contents: $service_file"
        sed -i.bak 's/xyz\.immortius\.chunkbychunk/com.ryvione.chunkbychunk/g' "$service_file"
        rm -f "${service_file}.bak"
    fi
done

echo ""
echo "=================================="
echo "Migration Complete!"
echo "=================================="
echo ""
echo "Changes applied:"
echo "  ✓ Copyright headers added to all Java files"
echo "  ✓ Comments removed from all Java files"
echo "  ✓ Package names changed to com.ryvione.chunkbychunk"
echo "  ✓ Directory structure updated"
echo "  ✓ Resource files updated"
echo "  ✓ Build files updated"
echo "  ✓ Service files updated"
echo ""
echo "IMPORTANT - Manual verification needed:"
echo ""
echo "  1. Check that fabric.mod.json looks correct"
echo "  2. Check that build.gradle files have group = 'com.ryvione'"
echo "  3. Update README.md"
echo "  4. Test build with: ./gradlew build"
echo "  5. If build succeeds, commit:"
echo "     git add ."
echo "     git commit -m 'Fork from immortius/chunkbychunk - Updated for latest MC versions'"
echo "     git push"
echo ""
echo "=================================="