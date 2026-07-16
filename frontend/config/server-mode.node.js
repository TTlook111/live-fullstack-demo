// config/server-mode.node.js (Node.js后端专用)
const USE_MOCK_SERVER = process.env.USE_MOCK !== 'false'; // 默认使用 mock模式，设置 USE_MOCK=false 使用真实服务器
const LOCAL_SERVER_URL = 'http://localhost:8080';
// 部署服务器配置：使用环境变量或默认端口
const DEPLOY_PORT = process.env.PORT || 8080; // 部署端口，Render 会自动设置 PORT 环境变量
const REAL_SERVER_URL = process.env.REAL_SERVER_URL || `http://localhost:${DEPLOY_PORT}`; // 部署服务器地址
const REAL_SERVER_PORT = DEPLOY_PORT; // 部署服务器端口
// 后端服务器配置（真正的后端服务器地址）
const BACKEND_SERVER_URL = process.env.BACKEND_SERVER_URL || ''; // 后端服务器地址，通过环境变量配置
// 是否优先使用后端服务器（设为 true 时，所有 API 请求会优先代理到后端服务器）
// 注意：后台管理系统通过中间层代理访问后端服务器
// 🔧 通过环境变量控制：设置 PRIORITIZE_BACKEND=true 时，API请求代理到后端服务器
const PRIORITIZE_BACKEND_SERVER = process.env.PRIORITIZE_BACKEND === 'true'; // 默认使用本地 mock数据
const REAL_WECHAT_CONFIG = {
    appid: 'wx94289b0d2ca7a802',
    secret: '10409c1193a326a7b328f675b1776195'
};
const getLocalIP = () => '192.168.31.189';
const MOCK_SERVER_CONFIG = {
    host: getLocalIP(),
    port: DEPLOY_PORT, // 使用环境变量 PORT 或默认 8080
    url: `http://${getLocalIP()}:${DEPLOY_PORT}`
};
const getCurrentServerConfig = () => {
    // 无论哪种模式，都使用 Render 的 PORT 环境变量
    const port = process.env.PORT || 8080;
    if (USE_MOCK_SERVER) {
        return {
            mode: 'mock',
            url: `http://localhost:${port}`,
            host: '0.0.0.0',
            port: port,
            wechat: {
                useMock: true,
                appid: 'wx94289b0d2ca7a802',
                secret: '10409c1193a326a7b328f675b1776195'
            }
        };
    } else {
        // 使用真实服务器，部署模式
        return {
            mode: 'real',
            url: REAL_SERVER_URL,
            port: port,
            wechat: {
                useMock: false,
                appid: REAL_WECHAT_CONFIG.appid,
                secret: REAL_WECHAT_CONFIG.secret
            }
        };
    }
};
const printConfig = () => {
    const config = getCurrentServerConfig();
    console.log('═══════════════════════════════════════');
    console.log('📋 服务器配置信息');
    console.log('═══════════════════════════════════════');
    console.log(`模式: ${config.mode === 'mock' ? '🧪 模拟服务器' : '🌐 真实服务器'}`);
    console.log(`地址: ${config.url}`);
    if (config.mode === 'mock') {
        console.log(`本地访问: http://localhost:${config.port}`);
        console.log(`局域网访问: ${config.url}`);
    }
    console.log(`微信登录: ${config.wechat.useMock ? '模拟模式' : '真实模式'}`);
    if (!config.wechat.useMock) {
        console.log(`微信 AppID: ${config.wechat.appid}`);
        console.log(`微信 Secret: ${config.wechat.secret ? config.wechat.secret.substring(0, 8) + '...' : '未设置'}`);
    }
    console.log('═══════════════════════════════════════');
};
module.exports = {
	USE_MOCK_SERVER,
	MOCK_SERVER_CONFIG,
	REAL_SERVER_URL,
	REAL_SERVER_PORT,
	REAL_WECHAT_CONFIG,
	BACKEND_SERVER_URL,
	PRIORITIZE_BACKEND_SERVER,
	getCurrentServerConfig,
	printConfig,
	LOCAL_SERVER_URL,
};
