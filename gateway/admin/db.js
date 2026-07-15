/**
 * 数据库模块（Mock版本）
 * 本地测试使用，不连接真实数据库
 */

// 内存存储
const data = {
    users: [],
    comments: [],
    votes: [],
    streams: []
};

// 基本CRUD操作
module.exports = {
    // 用户操作
    getUsers: () => data.users,
    getUserById: (id) => data.users.find(u => u.id === id),
    createUser: (user) => {
        user.id = Date.now();
        data.users.push(user);
        return user;
    },

    // 评论操作
    getComments: () => data.comments,
    getCommentById: (id) => data.comments.find(c => c.id === id),
    createComment: (comment) => {
        comment.id = Date.now();
        data.comments.push(comment);
        return comment;
    },

    // 投票操作
    getVotes: () => data.votes,
    createVote: (vote) => {
        vote.id = Date.now();
        data.votes.push(vote);
        return vote;
    },

    // 直播流操作
    getStreams: () => data.streams,
    getStreamById: (id) => data.streams.find(s => s.id === id),
    createStream: (stream) => {
        stream.id = Date.now();
        data.streams.push(stream);
        return stream;
    }
};
