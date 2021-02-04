if( redis.call('exists',KEYS[1])==1 )
then
    local value = redis.call('GET',KEYS[1]);
    local stock = tonumber(value);
    if( stock > 0 )
    then
        return redis.call('DECR',KEYS[1]);
    else
        return -1;
    end
else
    return -1;
end