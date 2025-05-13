#!/usr/bin/env bash
set -euo pipefail

: "${USERNAME:?USERNAME not set or empty}"
: "${REPO:?REPO not set or empty}"
: "${TAG:?TAG not set or empty}"

CONTEXT="${1:-.}"
shift || true

BUILDER_NAME="multiarch-builder"


if ! docker buildx inspect "$BUILDER_NAME" >/dev/null 2>&1; then
  echo "🔧  Creating buildx builder '$BUILDER_NAME' with docker-container driver…"
  docker buildx create --name "$BUILDER_NAME" --driver docker-container --use
else
  docker buildx use "$BUILDER_NAME"
fi


if ! docker buildx inspect --bootstrap | grep -q 'linux/arm64'; then
  echo "🔧  Registering binfmt for cross-arch builds…"
  docker run --privileged --rm tonistiigi/binfmt:latest --install all
fi

docker buildx build \
  --platform linux/amd64,linux/arm64 \
  -t "${USERNAME}/${REPO}:${TAG}" \
  -t "${USERNAME}/${REPO}:latest" \
  "${@}" \
  --push \
  "$CONTEXT"
