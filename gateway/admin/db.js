/**
 * 数据库模块（Mock版本）
 * 本地测试使用，不连接真实数据库
 * 提供 gateway.js 所需的完整嵌套 API 结构
 */

// 内存存储
const data = {
    users: [],
    streams: [
        {
            id: 'stream-001',
            name: '测试直播流',
            url: 'http://192.168.31.249:8086/live/stream.m3u8',
            type: 'hls',
            description: '默认测试直播流',
            enabled: true,
            createdAt: new Date().toISOString(),
            updatedAt: new Date().toISOString()
        }
    ],
    debate: {
        id: 'debate-default-001',
        title: '如果有一个能一键消除痛苦的按钮，你会按吗？',
        description: '这是一个关于痛苦、成长与人性选择的深度辩论',
        leftPosition: '按下去，消除痛苦',
        rightPosition: '不按，痛苦是成长的一部分'
    },
    liveSchedule: {
        isScheduled: false,
        scheduledStartTime: null,
        scheduledEndTime: null,
        streamId: null
    },
    statistics: {
        totalVotes: 0,
        totalComments: 0,
        totalLikes: 0,
        lastLiveTime: null,
        liveDuration: 0,
        dailyStats: []
    }
};

module.exports = {
    // 用户操作
    users: {
        getAll: () => data.users,
        getById: (id) => data.users.find(u => u.id === id),
        createOrUpdate: (userInfo) => {
            const existing = data.users.find(u => u.id === userInfo.id);
            if (existing) {
                Object.assign(existing, userInfo, { updatedAt: new Date().toISOString() });
                return existing;
            }
            const newUser = {
                ...userInfo,
                createdAt: new Date().toISOString(),
                updatedAt: new Date().toISOString(),
                statistics: {
                    totalVotes: 0,
                    totalComments: 0,
                    totalLikes: 0,
                    currentPosition: 'neutral'
                }
            };
            data.users.push(newUser);
            return newUser;
        },
        updateStats: (userId, stats) => {
            const user = data.users.find(u => u.id === userId);
            if (user) {
                if (!user.statistics) {
                    user.statistics = { totalVotes: 0, totalComments: 0, totalLikes: 0 };
                }
                Object.assign(user.statistics, stats);
                user.updatedAt = new Date().toISOString();
            }
            return user;
        }
    },

    // 辩论设置
    debate: {
        get: () => ({ ...data.debate }),
        update: (updates) => {
            Object.assign(data.debate, updates, { updatedAt: new Date().toISOString() });
            return { ...data.debate };
        }
    },

    // 直播计划
    liveSchedule: {
        get: () => ({ ...data.liveSchedule }),
        update: (scheduleData) => {
            Object.assign(data.liveSchedule, scheduleData, { updatedAt: new Date().toISOString() });
            return { ...data.liveSchedule };
        },
        clear: () => {
            data.liveSchedule.isScheduled = false;
            data.liveSchedule.scheduledStartTime = null;
            data.liveSchedule.scheduledEndTime = null;
            data.liveSchedule.streamId = null;
            return { ...data.liveSchedule };
        }
    },

    // 直播流操作
    streams: {
        getAll: () => [...data.streams],
        getById: (id) => data.streams.find(s => s.id === id) || null,
        getActive: () => data.streams.find(s => s.enabled) || null,
        add: (stream) => {
            data.streams.push(stream);
            return stream;
        },
        update: (id, updates) => {
            const stream = data.streams.find(s => s.id === id);
            if (stream) {
                Object.assign(stream, updates);
                return stream;
            }
            return null;
        },
        delete: (id) => {
            const index = data.streams.findIndex(s => s.id === id);
            if (index !== -1) {
                return data.streams.splice(index, 1)[0];
            }
            return null;
        }
    },

    // 统计数据
    statistics: {
        get: () => ({ ...data.statistics }),
        getDashboard: () => ({
            ...data.statistics,
            isLive: false
        }),
        updateDashboard: (updates) => {
            Object.assign(data.statistics, updates);
            return { ...data.statistics };
        },
        incrementVotes: (count) => {
            data.statistics.totalVotes = (data.statistics.totalVotes || 0) + (count || 1);
            return data.statistics.totalVotes;
        }
    }
};
