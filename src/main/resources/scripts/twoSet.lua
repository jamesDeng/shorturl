local ok = redis.call('set', KEYS[1],KEYS[2])

ok = redis.call('set', KEYS[3],KEYS[4])

return ok