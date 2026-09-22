# PebbleCommons

Shared modules bundled with Pebblin. This component originated as the
[PebbleCommons project](https://github.com/matejdro/PebbleCommons) and retains
its original license and source notices.

## Current implementation status

### Bluetooth common

Implemented and used by Pebblin:

- `PacketQueue` for serialized Pebble sends, delivery confirmation, retries,
  and unrecoverable-transfer reporting.
- `WatchAppConnection` and `WatchappConnectionsManager` for lifecycle and
  connection management across watches.
- Typed Pebble dictionary helpers, bounded string encoding, unsigned Okio
  helpers, and test fakes.
- Shared notification transport primitives, including typed notification sends
  and delivery completion.
- Shared low-level interactive packet sending through the watch connection.

The shared library intentionally owns transport concerns, not Pebblin's
product-specific interactive protocol. Pebblin currently owns the list and
confirmation message model, session validation, Tasker result mapping, and
watch UI.

### Bucket sync

Implemented:

- Bucket repository and update model.
- Watch sync loop and watchapp-open control.
- Background sync workers and foreground/background notifications.
- SQLDelight-backed persistence with in-memory test fakes.

### Not implemented here

- A generic interactive-session API shared by multiple apps.
- Generic list/confirmation domain models or UI.
- Tasker integration and Tasker variable declarations.
- Pebble watchapp UI, packet definitions, or app-specific protocol handling.
- Release packaging and app-specific build/versioning.

## Next steps

1. Keep the transport and sync APIs stable while Pebblin exercises them on
   aplite and larger Pebble models.
2. Extract a generic interactive-session abstraction only after a second
   consumer needs it; avoid moving Pebblin-specific protocol code prematurely.
3. Add a small compatibility test matrix for PebbleKit integer normalization and
   delivery/retry behavior as additional consumers arrive.
4. Document and version shared API changes before updating downstream apps.

PebbleCommons is integrated into Pebblin as ordinary source under this
repository. The Android build still includes its modules through the relative
project paths in `mobile/settings.gradle.kts`.
