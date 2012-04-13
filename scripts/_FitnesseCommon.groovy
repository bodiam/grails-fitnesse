printMessage = { String message -> event('StatusUpdate', [message]) }
errorMessage = { String message -> event('StatusError', [message]) }