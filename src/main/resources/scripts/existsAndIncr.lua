local mapKey   = KEYS[1]
local sequenceKey   = KEYS[2]

local ok = redis.call('EXISTS', mapKey)

if ok == 0 then
  return redis.call('INCR', sequenceKey)
end

return 0