"""build_single_file.py — concatenate the WORKSPACE_MEMORY topic files into ONE
markdown file (CIDM_MAV_COMPLETE_MEMORY.md) for loading into a meeting assistant
or any LLM context.

Usage (from repo root or this folder):
    python WORKSPACE_MEMORY/build_single_file.py            # writes CIDM_MAV_COMPLETE_MEMORY.md
    python WORKSPACE_MEMORY/build_single_file.py --check    # print size/section stats only

Order = README first, then numbered files ascending, then CHANGELOG. Add a new
topic file as NN_name.md and it is picked up automatically.
"""
import datetime
import pathlib
import re
import sys

HERE = pathlib.Path(__file__).resolve().parent
OUT = HERE / "CIDM_MAV_COMPLETE_MEMORY.md"


def ordered_files():
    numbered = sorted(p for p in HERE.glob("[0-9][0-9]_*.md"))
    files = []
    readme = HERE / "README.md"
    if readme.exists():
        files.append(readme)
    files.extend(numbered)
    changelog = HERE / "CHANGELOG.md"
    if changelog.exists():
        files.append(changelog)
    return files


def build():
    files = ordered_files()
    parts = []
    toc = []
    for f in files:
        text = f.read_text(encoding="utf-8").rstrip() + "\n"
        title_match = re.search(r"^#\s+(.*)$", text, re.M)
        title = title_match.group(1).strip() if title_match else f.stem
        anchor = re.sub(r"[^a-z0-9]+", "-", title.lower()).strip("-")
        toc.append(f"- [{title}](#{anchor})  ·  `{f.name}`")
        parts.append(f"\n\n<!-- ===== SOURCE FILE: {f.name} ===== -->\n\n{text}")
    stamp = datetime.date.today().isoformat()
    header = (
        "# CIDM MAV — COMPLETE WORKSPACE MEMORY (single-file pack)\n\n"
        f"_Generated {stamp} by `WORKSPACE_MEMORY/build_single_file.py` from "
        f"{len(files)} source files. Edit the source files, not this file._\n\n"
        "## Table of contents\n\n" + "\n".join(toc) + "\n"
    )
    body = header + "".join(parts)
    return body, files


def main():
    body, files = build()
    if "--check" in sys.argv:
        print(f"{len(files)} files, {len(body):,} chars, {len(body.splitlines()):,} lines")
        for f in files:
            print(f"  {f.name:45s} {f.stat().st_size:>8,} bytes")
        return
    OUT.write_text(body, encoding="utf-8")
    print(f"Wrote {OUT} ({len(body):,} chars, {len(body.splitlines()):,} lines, {len(files)} files)")


if __name__ == "__main__":
    main()
